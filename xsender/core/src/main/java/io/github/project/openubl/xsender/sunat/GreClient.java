package io.github.project.openubl.xsender.sunat;

import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;

import java.net.URI;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
    SunatResponse sendDocument(String url, ZipFile zipFile, CompanyCredentials credentials) {
        String name = zipFile.getFilename().replaceFirst("\\.zip$", "");
        String payload = "{\"archivo\":{\"nomArchivo\":%s,\"arcGreZip\":%s,\"hashZip\":%s}}".formatted(
                Json.quote(zipFile.getFilename()),
                Json.quote(Base64.getEncoder().encodeToString(zipFile.getFile())),
                Json.quote(sha256Hex(zipFile.getFile()))
        );

        HttpResponse<byte[]> response = sendWithToken(credentials, Http.request(URI.create(url + "/comprobantes/" + name))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload)));

        Map<String, Object> json = parseJson(response);
        return text(json.get("numTicket"))
                .map(SunatResponses::ticket)
                .or(() -> restError(json))
                .orElseThrow(() -> Http.unexpectedResponse(response));
    }

    /** GET {url}/comprobantes/envios/{ticket} */
    SunatResponse verifyTicket(String url, String ticket, CompanyCredentials credentials) {
        HttpResponse<byte[]> response = sendWithToken(credentials, Http.request(URI.create(url + "/comprobantes/envios/" + ticket))
                .GET());

        Map<String, Object> json = parseJson(response);
        Optional<String> statusCode = text(json.get("codRespuesta"));
        if (statusCode.isEmpty()) {
            return restError(json).orElseThrow(() -> Http.unexpectedResponse(response));
        }

        Optional<String> cdr = text(json.get("arcCdr"));
        if (cdr.isPresent()) {
            return SunatResponses.cdr(Base64.getDecoder().decode(cdr.get()));
        }
        if (json.get("error") instanceof Map<?, ?> error && text(error.get("numError")).isPresent()) {
            return SunatResponses.status(text(error.get("numError")).get(), text(error.get("desError")).orElse(null), List.of());
        }
        return SunatResponses.status(statusCode.get(), null, List.of());
    }

    private HttpResponse<byte[]> sendWithToken(CompanyCredentials credentials, HttpRequest.Builder request) {
        HttpResponse<byte[]> response = Http.send(httpClient, request
                .header("Authorization", "Bearer " + accessToken(credentials))
                .build());
        if (response.statusCode() == 401) {
            // SUNAT may revoke a token before it expires; don't keep reusing it
            tokens.remove(tokenKey(credentials));
        }
        return response;
    }

    private String accessToken(CompanyCredentials credentials) {
        if (credentials.getClientId() == null || credentials.getClientSecret() == null) {
            throw new XSenderException("clientId and clientSecret are required to send guías de remisión");
        }

        String key = tokenKey(credentials);
        AccessToken cached = tokens.get(key);
        if (cached != null && Instant.now().isBefore(cached.reuseUntil())) {
            return cached.value();
        }

        AccessToken token = requestAccessToken(credentials);
        tokens.put(key, token);
        return token.value();
    }

    private AccessToken requestAccessToken(CompanyCredentials credentials) {
        String form = formEncode(Map.of(
                "grant_type", "password",
                "scope", TOKEN_SCOPE,
                "client_id", credentials.getClientId(),
                "client_secret", credentials.getClientSecret(),
                "username", credentials.getUsername(),
                "password", credentials.getPassword()
        ));
        URI uri = URI.create(securityApiUrl + "/v1/clientessol/" + credentials.getClientId() + "/oauth2/token/");
        HttpResponse<byte[]> response = Http.send(httpClient, Http.request(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build());

        Map<String, Object> json = parseJson(response);
        String token = text(json.get("access_token")).orElseThrow(() -> Http.unexpectedResponse(response));
        long expiresIn = text(json.get("expires_in")).map(Long::parseLong).orElse(DEFAULT_TOKEN_LIFETIME_SECONDS);
        return new AccessToken(token, Instant.now().plusSeconds(expiresIn - TOKEN_EXPIRY_MARGIN_SECONDS));
    }

    /**
     * SUNAT's REST error body: {@code {cod|status, msg|message, errors: [{cod, msg}]}}.
     * The detailed errors become the notes, as "cod - msg".
     */
    private static Optional<SunatResponse> restError(Map<String, Object> json) {
        Optional<String> code = text(json.get("cod")).or(() -> text(json.get("status")));
        if (code.isEmpty()) {
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

    /** A non-empty string or number */
    private static Optional<String> text(Object value) {
        return value instanceof String string && !string.isEmpty() ? Optional.of(string) : Optional.empty();
    }

    private static String tokenKey(CompanyCredentials credentials) {
        return credentials.getClientId() + ":" + credentials.getUsername();
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
