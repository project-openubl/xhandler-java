package io.github.project.openubl.xsender.sunat;

import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SUNAT's REST API for guías de remisión (GRE): the zipped document is sent,
 * and its ticket checked, with an OAuth access token.
 */
final class GreClient {

    private static final String TOKEN_SCOPE = "https://api-cpe.sunat.gob.pe";
    /** A token is not reused during its last minute */
    private static final long TOKEN_EXPIRY_MARGIN_SECONDS = 60;
    private static final long DEFAULT_TOKEN_LIFETIME_SECONDS = 3600;

    private final HttpClient httpClient;
    private final String securityApiUrl;
    private final Map<String, AccessToken> tokens = new ConcurrentHashMap<>();

    private record AccessToken(String value, Instant reuseUntil) {
    }

    GreClient(HttpClient httpClient, String securityApiUrl) {
        this.httpClient = httpClient;
        this.securityApiUrl = securityApiUrl;
    }

    /** POST {url}/comprobantes/{name}; SUNAT answers with a ticket */
    SunatResponse sendDocument(BillServiceDestination destination, ZipFile zipFile, CompanyCredentials credentials) {
        String name = zipFile.getFilename().replaceFirst("\\.zip$", "");
        String payload = "{\"archivo\":{\"nomArchivo\":%s,\"arcGreZip\":%s,\"hashZip\":%s}}".formatted(
                Json.quote(zipFile.getFilename()),
                Json.quote(Base64.getEncoder().encodeToString(zipFile.getFile())),
                Json.quote(sha256Hex(zipFile.getFile()))
        );
        String url = destination.getUrl() + destination.getRestOperation().getPath() + "/" + pathSegment(name);

        HttpResponse<byte[]> response = sendWithToken(credentials, token -> Http.request(url)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build());

        Map<String, Object> json = parseJson(response);
        return text(json.get("numTicket"))
                .map(SunatResponses::ticket)
                .or(() -> restError(response, json))
                .orElseThrow(() -> Http.unexpectedResponse(response));
    }

    /** GET {url}/comprobantes/envios/{ticket} */
    SunatResponse verifyTicket(BillServiceDestination destination, String ticket, CompanyCredentials credentials) {
        String url = destination.getUrl() + destination.getRestOperation().getPath() + "/" + pathSegment(ticket);

        HttpResponse<byte[]> response = sendWithToken(credentials, token -> Http.request(url)
                .header("Authorization", "Bearer " + token)
                .GET()
                .build());

        Map<String, Object> json = parseJson(response);
        Optional<String> statusCode = text(json.get("codRespuesta"));
        if (statusCode.isEmpty()) {
            return restError(response, json).orElseThrow(() -> Http.unexpectedResponse(response));
        }

        Optional<String> cdr = text(json.get("arcCdr"));
        if (cdr.isPresent()) {
            return SunatResponses.cdr(Base64.getMimeDecoder().decode(cdr.get()));
        }
        if (json.get("error") instanceof Map<?, ?> error) {
            Optional<SunatResponse> rejection = text(error.get("numError"))
                    .map(code -> SunatResponses.status(code, text(error.get("desError")).orElse(null), List.of()));
            if (rejection.isPresent()) {
                return rejection.get();
            }
        }
        return SunatResponses.status(statusCode.get(), null, List.of());
    }

    /**
     * A 401 means SUNAT revoked the token before it expired and did not process
     * the request, so it is retried once with a new token.
     */
    private HttpResponse<byte[]> sendWithToken(CompanyCredentials credentials, Function<String, HttpRequest> request) {
        String token = accessToken(credentials);
        HttpResponse<byte[]> response = Http.send(httpClient, request.apply(token));
        if (response.statusCode() == 401 && credentials.getToken() == null) {
            forgetToken(credentials, token);
            response = Http.send(httpClient, request.apply(accessToken(credentials)));
        }
        return response;
    }

    /**
     * The token given in the credentials, if any, or one requested with the
     * SOL user and the API client id/secret and reused until it is about to expire.
     */
    private String accessToken(CompanyCredentials credentials) {
        if (credentials.getToken() != null) {
            return credentials.getToken();
        }
        if (credentials.getClientId() == null || credentials.getClientSecret() == null
                || credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new XSenderException("username, password, clientId and clientSecret are required to send guías de remisión");
        }

        AccessToken cached = tokens.get(tokenKey(credentials));
        if (cached != null && Instant.now().isBefore(cached.reuseUntil())) {
            return cached.value();
        }

        AccessToken token = requestAccessToken(credentials);
        tokens.put(tokenKey(credentials), token);
        return token.value();
    }

    /** Only if no other thread replaced it with a new one meanwhile */
    private void forgetToken(CompanyCredentials credentials, String token) {
        tokens.computeIfPresent(tokenKey(credentials), (key, cached) -> cached.value().equals(token) ? null : cached);
    }

    private AccessToken requestAccessToken(CompanyCredentials credentials) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("grant_type", "password");
        form.put("scope", TOKEN_SCOPE);
        form.put("client_id", credentials.getClientId());
        form.put("client_secret", credentials.getClientSecret());
        form.put("username", credentials.getUsername());
        form.put("password", credentials.getPassword());

        String url = securityApiUrl + "/v1/clientessol/" + pathSegment(credentials.getClientId()) + "/oauth2/token/";
        HttpResponse<byte[]> response = Http.send(httpClient, Http.request(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formEncode(form)))
                .build());

        Map<String, Object> json = parseJson(response);
        String token = text(json.get("access_token")).orElseThrow(() -> Http.unexpectedResponse(response));
        long expiresIn = text(json.get("expires_in")).map(GreClient::seconds).orElse(DEFAULT_TOKEN_LIFETIME_SECONDS);
        return new AccessToken(token, Instant.now().plusSeconds(Math.max(0, expiresIn - TOKEN_EXPIRY_MARGIN_SECONDS)));
    }

    /**
     * SUNAT's REST error body for requests it refuses (4xx):
     * {@code {cod|status, msg|message, errors: [{cod, msg}]}}.
     * The detailed errors become the notes, as "cod - msg".
     * A server error (5xx) is not an answer about the document, so it is not mapped.
     */
    private static Optional<SunatResponse> restError(HttpResponse<byte[]> response, Map<String, Object> json) {
        Optional<String> code = text(json.get("cod")).or(() -> text(json.get("status")));
        if (response.statusCode() >= 500 || code.isEmpty()) {
            return Optional.empty();
        }

        String description = text(json.get("msg")).or(() -> text(json.get("message"))).orElse(null);
        List<String> notes = json.get("errors") instanceof List<?> errors
                ? errors.stream()
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(error -> text(error.get("cod")).orElse("") + " - " + text(error.get("msg")).orElse(""))
                .toList()
                : List.of();
        return Optional.of(SunatResponses.status(code.get(), description, notes));
    }

    private static Map<String, Object> parseJson(HttpResponse<byte[]> response) {
        return Json.parseObject(new String(response.body(), StandardCharsets.UTF_8))
                .orElseThrow(() -> Http.unexpectedResponse(response));
    }

    /** A non-empty string; {@link Json} also returns numbers as strings */
    private static Optional<String> text(Object value) {
        return value instanceof String string && !string.isEmpty() ? Optional.of(string) : Optional.empty();
    }

    /** "3600" or "3600.0" */
    private static long seconds(String value) {
        try {
            return (long) Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return DEFAULT_TOKEN_LIFETIME_SECONDS;
        }
    }

    private static String tokenKey(CompanyCredentials credentials) {
        return credentials.getClientId() + ":" + credentials.getUsername();
    }

    private static String pathSegment(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static String formEncode(Map<String, String> fields) {
        return fields.entrySet().stream()
                .map(field -> field.getKey() + "=" + URLEncoder.encode(field.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    private static String sha256Hex(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is always available", e);
        }
    }
}
