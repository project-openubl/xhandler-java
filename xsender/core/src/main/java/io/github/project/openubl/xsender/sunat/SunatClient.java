package io.github.project.openubl.xsender.sunat;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xhandler.core.ErrorKind;
import io.github.project.openubl.xhandler.core.Xhandler;
import io.github.project.openubl.xhandler.core.XhandlerException;
import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
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
 * faults); this client only sends them with {@link HttpClient}.
 * <p>
 * Thread-safe and meant to be shared (one per application): it holds the HTTP
 * connection pool and caches the guías de remisión access tokens.
 */
public class SunatClient {

    public static final String SUNAT_SECURITY_API_URL = "https://api-seguridad.sunat.gob.pe";
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
        this.httpClient = httpClient;
        this.securityApiUrl = securityApiUrl;
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
    public SunatResponse sendFile(BillServiceDestination destination, ZipFile zipFile, CompanyCredentials credentials) {
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
    public SunatResponse verifyTicket(BillServiceDestination destination, String ticket, CompanyCredentials credentials) {
        Map<String, Object> call = Map.of("verify_ticket", Map.of(
                "target", Destinations.target(destination),
                "ticket", ticket
        ));
        return SunatResponses.of(exchange(call, destination.getRestOperation() != null, credentials));
    }

    /**
     * Consults an issued document: its status (getStatus) or its CDR (getStatusCdr).
     */
    public ConsultResponse consultDocument(BillConsultServiceDestination destination, DocumentQuery query, CompanyCredentials credentials) {
        String operation = destination.getOperation() == BillConsultServiceDestination.Operation.GET_STATUS
                ? "get_status"
                : "get_status_cdr";
        Map<String, Object> document = Map.of(
                "ruc", query.ruc(),
                "tipo_comprobante", query.documentType(),
                "serie", query.serie(),
                "numero", query.number()
        );
        Map<String, Object> call = Map.of(operation, Map.of("url", destination.getUrl(), "document", document));
        return SunatResponses.consult(exchange(call, false, credentials));
    }

    /**
     * Validates a document against the given criteria (validaCDPcriterios).
     */
    public ConsultResponse validateCdp(BillValidServiceDestination destination, CdpQuery query, CompanyCredentials credentials) {
        Map<String, Object> cdp = new HashMap<>();
        cdp.put("ruc", query.ruc());
        cdp.put("tipo_comprobante", query.documentType());
        cdp.put("serie", query.serie());
        cdp.put("numero", Long.parseLong(query.number()));
        cdp.put("tipo_documento_identidad_receptor", query.receiverIdType());
        cdp.put("numero_documento_identidad_receptor", query.receiverIdNumber());
        cdp.put("fecha_emision", query.issueDate());
        cdp.put("importe_total", query.total().toPlainString());
        cdp.put("numero_autorizacion", query.authorizationNumber());
        Map<String, Object> call = Map.of("validate_cdp", Map.of("url", destination.getUrl(), "cdp", cdp));
        return SunatResponses.consult(exchange(call, false, credentials));
    }

    /**
     * Validates an XML file (verificaCPEarchivo).
     */
    public ConsultResponse validateFile(BillValidServiceDestination destination, String filename, byte[] file, CompanyCredentials credentials) {
        Map<String, Object> call = Map.of("validate_file", Map.of("url", destination.getUrl(), "file", file(filename, file)));
        return SunatResponses.consult(exchange(call, false, credentials));
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
    private JsonNode exchange(Map<String, Object> call, boolean rest, CompanyCredentials credentials) {
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
                return Xhandler.invoke("parse", parseInput(call, response));
            } catch (XhandlerException e) {
                if (e.getKind() != ErrorKind.RATE_LIMITED || attempt == RATE_LIMIT_PAUSES.size()) {
                    throw unexpectedResponse(response, e);
                }
                Http.pause(RATE_LIMIT_PAUSES.get(attempt));
            }
        }
    }

    private JsonNode parse(Map<String, Object> call, HttpResponse<byte[]> response) {
        try {
            return Xhandler.invoke("parse", parseInput(call, response));
        } catch (XhandlerException e) {
            throw unexpectedResponse(response, e);
        }
    }

    private static Map<String, Object> parseInput(Map<String, Object> call, HttpResponse<byte[]> response) {
        return Map.of(
                "call", call,
                "status", response.statusCode(),
                "body", new String(response.body(), StandardCharsets.UTF_8)
        );
    }

    /** Builds the request of the call with the core and sends it */
    private HttpResponse<byte[]> send(Map<String, Object> call, CompanyCredentials credentials, String token) {
        Map<String, Object> input = new HashMap<>();
        input.put("call", call);
        input.put("credentials", credentialsJson(credentials));
        input.put("token", token);
        try {
            return Http.send(httpClient, Http.request(Xhandler.invoke("request", input)));
        } catch (XhandlerException e) {
            throw new XSenderException(e.getMessage(), e);
        }
    }

    /**
     * The token given in the credentials, if any, or one requested with the
     * SOL user and the API client id/secret and reused until it is about to expire.
     */
    private String accessToken(CompanyCredentials credentials) {
        if (credentials.getToken() != null) {
            return credentials.getToken();
        }
        AccessToken cached = tokens.get(tokenKey(credentials));
        if (cached != null && Instant.now().isBefore(cached.reuseUntil())) {
            return cached.value();
        }
        Map<String, Object> call = Map.of("token", Map.of("url", securityApiUrl));
        JsonNode token = exchange(call, false, credentials).get("token");
        long reuseFor = Math.max(0, token.get("expires_in").asLong() - 60);
        AccessToken accessToken = new AccessToken(token.get("access_token").asText(), Instant.now().plusSeconds(reuseFor));
        tokens.put(tokenKey(credentials), accessToken);
        return accessToken.value();
    }

    private static String tokenKey(CompanyCredentials credentials) {
        return credentials.getClientId() + ":" + credentials.getUsername();
    }

    private static Map<String, Object> credentialsJson(CompanyCredentials credentials) {
        Map<String, Object> json = new HashMap<>();
        json.put("username", Objects.toString(credentials.getUsername(), ""));
        json.put("password", Objects.toString(credentials.getPassword(), ""));
        json.put("client_id", credentials.getClientId());
        json.put("client_secret", credentials.getClientSecret());
        return json;
    }

    private static XSenderException unexpectedResponse(HttpResponse<byte[]> response, XhandlerException cause) {
        XSenderException exception = Http.unexpectedResponse(response);
        exception.initCause(cause);
        return exception;
    }
}
