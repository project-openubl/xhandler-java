package io.github.project.openubl.xsender.sunat;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xhandler.core.ErrorKind;
import io.github.project.openubl.xhandler.core.Xhandler;
import io.github.project.openubl.xhandler.core.XhandlerException;
import io.github.project.openubl.xsender.Credentials;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Calls SUNAT's services: the SOAP bill, consult and validation services, and
 * the REST API for guías de remisión.
 * <p>
 * The xhandler core builds every request and reads every answer (CDR, tickets,
 * faults); this client only sends them with {@link HttpClient}. Errors of the
 * core are {@link XhandlerException}s with their {@link ErrorKind} (e.g.
 * {@code RESPONSE} for an answer that can't be read, {@code RATE_LIMITED} when
 * SUNAT kept refusing the request); SUNAT that can't be reached is an
 * {@link java.io.UncheckedIOException}.
 * <p>
 * Thread-safe and meant to be shared (one per application): it holds the HTTP
 * connection pool and caches the guías de remisión access tokens.
 */
public class SunatClient {

    public static final String SUNAT_SECURITY_API_URL = "https://api-seguridad.sunat.gob.pe";
    /** Looks up documents already sent (getStatus, getStatusCdr) */
    public static final String BILL_CONSULT_SERVICE_URL = "https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";
    /** Validates documents (validateCdp, validateFile) */
    public static final String BILL_VALID_SERVICE_URL = "https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService";
    /** Pauses before retrying a request SUNAT's front proxy refused (rate limiting) */
    private static final List<Duration> RATE_LIMIT_PAUSES = List.of(Duration.ofSeconds(1), Duration.ofSeconds(2));

    private final HttpClient httpClient;
    private final String securityApiUrl;
    private final Map<String, AccessToken> tokens = new ConcurrentHashMap<>();

    private record AccessToken(String value, Instant reuseUntil) {
    }

    public SunatClient() {
        this(SUNAT_SECURITY_API_URL);
    }

    /**
     * @param securityApiUrl where the guías de remisión access tokens are requested
     */
    public SunatClient(String securityApiUrl) {
        this(defaultHttpClient(), securityApiUrl);
    }

    /**
     * @param httpClient     prefer {@link HttpClient.Version#HTTP_1_1}: SUNAT's front proxy often rejects HTTP/2 requests
     * @param securityApiUrl where the guías de remisión access tokens are requested
     */
    public SunatClient(HttpClient httpClient, String securityApiUrl) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.securityApiUrl = Objects.requireNonNull(securityApiUrl, "securityApiUrl");
    }

    /**
     * HTTP/1.1: SUNAT's front proxy often rejects HTTP/2 requests with a 401 page
     */
    static HttpClient defaultHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    /**
     * Sends a zipped document: sendBill/sendSummary/sendPack, or the REST API for guías de remisión.
     *
     * @return the CDR, a ticket to verify later, or SUNAT's error
     */
    public SunatResponse sendFile(BillServiceDestination destination, ZipFile zipFile, Credentials credentials) {
        Map<String, Object> call = Map.of("send_file", Map.of(
                "target", Destinations.target(destination),
                "file", file(zipFile.getFilename(), zipFile.getFile())
        ));
        return SunatResponses.of(exchange(call, destination.getRestOperation() != null, credentials));
    }

    /**
     * Checks the ticket SUNAT returned for a summary, voided documents or a guía de remisión.
     *
     * @return the CDR once processed, status {@code EN_PROCESO} meanwhile, or SUNAT's error
     */
    public SunatResponse verifyTicket(BillServiceDestination destination, String ticket, Credentials credentials) {
        Map<String, Object> call = Map.of("verify_ticket", Map.of(
                "target", Destinations.target(destination),
                "ticket", ticket
        ));
        return SunatResponses.of(exchange(call, destination.getRestOperation() != null, credentials));
    }

    /**
     * billConsultService getStatus: the status of a document already sent.
     *
     * @param url e.g. {@link #BILL_CONSULT_SERVICE_URL}
     */
    public ConsultResponse getStatus(String url, DocumentQuery query, Credentials credentials) {
        return consult("get_status", url, query, credentials);
    }

    /**
     * billConsultService getStatusCdr: the CDR of a document already sent.
     *
     * @param url e.g. {@link #BILL_CONSULT_SERVICE_URL}
     */
    public ConsultResponse getStatusCdr(String url, DocumentQuery query, Credentials credentials) {
        return consult("get_status_cdr", url, query, credentials);
    }

    private ConsultResponse consult(String operation, String url, DocumentQuery query, Credentials credentials) {
        Map<String, Object> document = Map.of(
                "ruc", query.ruc(),
                "tipo_comprobante", query.documentType(),
                "serie", query.serie(),
                "numero", query.number()
        );
        Map<String, Object> call = Map.of(operation, Map.of("url", url, "document", document));
        return SunatResponses.consult(exchange(call, false, credentials));
    }

    /**
     * billValidService validaCDPcriterios: validates a document against the given criteria.
     *
     * @param url e.g. {@link #BILL_VALID_SERVICE_URL}
     */
    public ConsultResponse validateCdp(String url, CdpQuery query, Credentials credentials) {
        Map<String, Object> cdp = new HashMap<>();
        cdp.put("ruc", query.ruc());
        cdp.put("tipo_comprobante", query.documentType());
        cdp.put("serie", query.serie());
        cdp.put("numero", number(query.number()));
        cdp.put("tipo_documento_identidad_receptor", query.receiverIdType());
        cdp.put("numero_documento_identidad_receptor", query.receiverIdNumber());
        cdp.put("fecha_emision", query.issueDate());
        cdp.put("importe_total", query.total().toPlainString());
        cdp.put("numero_autorizacion", query.authorizationNumber());
        Map<String, Object> call = Map.of("validate_cdp", Map.of("url", url, "cdp", cdp));
        return SunatResponses.consult(exchange(call, false, credentials));
    }

    /**
     * billValidService verificaCPEarchivo: validates an XML file.
     *
     * @param url e.g. {@link #BILL_VALID_SERVICE_URL}
     */
    public ConsultResponse validateFile(String url, String filename, byte[] file, Credentials credentials) {
        Map<String, Object> call = Map.of("validate_file", Map.of("url", url, "file", file(filename, file)));
        return SunatResponses.consult(exchange(call, false, credentials));
    }

    private static long number(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid document number: " + number, e);
        }
    }

    private static Map<String, Object> file(String filename, byte[] zip) {
        return Map.of("filename", filename, "zip", Base64.getEncoder().encodeToString(zip));
    }

    /**
     * Sends a call and returns its answer. SUNAT revokes the guías tokens before
     * they expire: a REST call rejected with a 401 did not happen, so it is retried
     * once with a new token. SUNAT's front proxy rate-limits SOAP calls with a
     * non-SOAP 401 page: the service never saw them, so they are retried after a
     * growing pause.
     */
    private JsonNode exchange(Map<String, Object> call, boolean rest, Credentials credentials) {
        if (rest) {
            String token = accessToken(credentials);
            HttpResponse<byte[]> response = send(call, credentials, token);
            if (response.statusCode() == 401 && credentials.getToken() == null) {
                tokens.computeIfPresent(tokenKey(credentials), (key, cached) -> cached.value().equals(token) ? null : cached);
                response = send(call, credentials, accessToken(credentials));
            }
            return parse(call, response);
        }

        for (int attempt = 0; ; attempt++) {
            HttpResponse<byte[]> response = send(call, credentials, null);
            try {
                return parse(call, response);
            } catch (XhandlerException e) {
                if (e.getKind() != ErrorKind.RATE_LIMITED || attempt == RATE_LIMIT_PAUSES.size()) {
                    throw e;
                }
                Http.pause(RATE_LIMIT_PAUSES.get(attempt));
            }
        }
    }

    /**
     * @throws XhandlerException e.g. {@link ErrorKind#RESPONSE} for an answer that can't be read
     */
    private static JsonNode parse(Map<String, Object> call, HttpResponse<byte[]> response) {
        return Xhandler.invoke("parse", parseInput(call, response));
    }

    private static Map<String, Object> parseInput(Map<String, Object> call, HttpResponse<byte[]> response) {
        List<List<String>> headers = new ArrayList<>();
        response.headers().map().forEach((name, values) -> values.forEach(value -> headers.add(List.of(name, value))));
        return Map.of(
                "call", call,
                "status", response.statusCode(),
                "headers", headers,
                "body", new String(response.body(), StandardCharsets.UTF_8)
        );
    }

    /** Builds the request of the call with the core and sends it */
    private HttpResponse<byte[]> send(Map<String, Object> call, Credentials credentials, String token) {
        Map<String, Object> input = new HashMap<>();
        input.put("call", call);
        input.put("credentials", credentialsJson(credentials));
        input.put("token", token);
        return Http.send(httpClient, Http.request(Xhandler.invoke("request", input)));
    }

    /**
     * The token given in the credentials, if any, or one requested with the
     * SOL user and the API client id/secret and reused until it is about to expire.
     */
    private String accessToken(Credentials credentials) {
        if (credentials.getToken() != null) {
            return credentials.getToken();
        }
        String key = tokenKey(credentials);
        AccessToken cached = tokens.get(key);
        if (cached != null && Instant.now().isBefore(cached.reuseUntil())) {
            return cached.value();
        }
        // One token request at a time, so concurrent calls don't revoke each other's tokens
        synchronized (tokens) {
            cached = tokens.get(key);
            if (cached != null && Instant.now().isBefore(cached.reuseUntil())) {
                return cached.value();
            }
            return requestAccessToken(key, credentials);
        }
    }

    private String requestAccessToken(String key, Credentials credentials) {
        Map<String, Object> call = Map.of("token", Map.of("url", securityApiUrl));
        JsonNode token = exchange(call, false, credentials).get("token");
        long reuseFor = Math.max(0, token.get("expires_in").asLong() - 60);
        AccessToken accessToken = new AccessToken(token.get("access_token").asText(), Instant.now().plusSeconds(reuseFor));
        tokens.put(key, accessToken);
        return accessToken.value();
    }

    private static String tokenKey(Credentials credentials) {
        return credentials.getClientId() + ":" + credentials.getUsername();
    }

    private static Map<String, Object> credentialsJson(Credentials credentials) {
        Map<String, Object> json = new HashMap<>();
        json.put("username", Objects.toString(credentials.getUsername(), ""));
        json.put("password", Objects.toString(credentials.getPassword(), ""));
        json.put("client_id", credentials.getClientId());
        json.put("client_secret", credentials.getClientSecret());
        return json;
    }
}
