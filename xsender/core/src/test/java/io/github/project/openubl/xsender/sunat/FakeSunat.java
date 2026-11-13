package io.github.project.openubl.xsender.sunat;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An HTTP server answering like SUNAT with canned responses, and recording the requests it gets.
 */
public class FakeSunat implements AutoCloseable {

    public record Request(String method, String path, Map<String, List<String>> headers, String body) {
        public String header(String name) {
            return headers.entrySet().stream()
                    .filter(header -> header.getKey().equalsIgnoreCase(name))
                    .map(header -> header.getValue().getFirst())
                    .findFirst()
                    .orElse(null);
        }
    }

    private record Response(int status, String contentType, String body) {
    }

    private final HttpServer server;
    private final Map<String, Deque<Response>> responses = new ConcurrentHashMap<>();
    private final List<Request> requests = new CopyOnWriteArrayList<>();

    public FakeSunat() {
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            requests.add(new Request(exchange.getRequestMethod(), path, exchange.getRequestHeaders(), body));

            Response response = nextResponse(path);
            byte[] responseBody = response.body().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", response.contentType());
            exchange.sendResponseHeaders(response.status(), responseBody.length);
            exchange.getResponseBody().write(responseBody);
            exchange.close();
        });
        server.start();
    }

    public String url(String path) {
        return "http://127.0.0.1:" + server.getAddress().getPort() + path;
    }

    public void respondXml(String path, int status, String resource) {
        respond(path, new Response(status, "text/xml", resource(resource)));
    }

    public void respondJson(String path, int status, String json) {
        respond(path, new Response(status, "application/json", json));
    }

    /** Answers the first request with the first response, and the following ones with the second */
    public void respondJsonSequence(String path, int firstStatus, String firstJson, int thenStatus, String thenJson) {
        respond(path, new Response(firstStatus, "application/json", firstJson), new Response(thenStatus, "application/json", thenJson));
    }

    private void respond(String path, Response... sequence) {
        responses.put(path, new ConcurrentLinkedDeque<>(List.of(sequence)));
    }

    /** The last response of a sequence answers every remaining request */
    private Response nextResponse(String path) {
        Deque<Response> sequence = responses.get(path);
        if (sequence == null) {
            return new Response(404, "text/html", "<html>Not found</html>");
        }
        return sequence.size() > 1 ? sequence.poll() : sequence.peek();
    }

    public List<Request> requests(String path) {
        return requests.stream().filter(request -> request.path().equals(path)).toList();
    }

    public Request lastRequest(String path) {
        return requests(path).getLast();
    }

    public static String resource(String name) {
        try (InputStream is = FakeSunat.class.getResourceAsStream(name)) {
            if (is == null) {
                throw new IllegalArgumentException("Missing test resource " + name);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
