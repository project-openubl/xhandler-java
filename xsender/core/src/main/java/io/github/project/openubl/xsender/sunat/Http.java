package io.github.project.openubl.xsender.sunat;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.project.openubl.xsender.XSenderException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

final class Http {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(120);

    private Http() {
    }

    static HttpRequest.Builder request(String url) {
        try {
            return HttpRequest.newBuilder(URI.create(url)).timeout(REQUEST_TIMEOUT);
        } catch (IllegalArgumentException e) {
            throw new XSenderException("Invalid SUNAT URL: " + url, e);
        }
    }

    /**
     * @param request the {@code {method, url, headers, body}} built by the xhandler core
     */
    static HttpRequest request(JsonNode request) {
        HttpRequest.Builder builder = request(request.get("url").asText());
        request.get("headers").forEach(header -> builder.header(header.get(0).asText(), header.get(1).asText()));
        JsonNode body = request.get("body");
        HttpRequest.BodyPublisher publisher = body.isNull()
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(body.asText());
        return builder.method(request.get("method").asText(), publisher).build();
    }

    static HttpResponse<byte[]> send(HttpClient httpClient, HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException e) {
            throw new XSenderException("Could not reach SUNAT at " + request.uri(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new XSenderException("Interrupted while calling SUNAT at " + request.uri(), e);
        }
    }

    static void pause(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new XSenderException("Interrupted while waiting to retry a SUNAT call", e);
        }
    }

    static XSenderException unexpectedResponse(HttpResponse<byte[]> response) {
        String body = new String(response.body(), StandardCharsets.UTF_8);
        String excerpt = body.length() > 500 ? body.substring(0, 500) + "..." : body;
        return new XSenderException(
                "Unexpected SUNAT response from " + response.uri() + ": HTTP " + response.statusCode() + " " + excerpt
        );
    }
}
