package io.github.project.openubl.quarkus.xsender.it;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;

/**
 * Answers like SUNAT's billService: sendBill with a CDR, sendSummary with a
 * ticket. The tests check the extension's wiring, so they don't depend on
 * SUNAT beta being up (core's SunatBetaTest talks to it, on demand).
 * Each integration-test module has its own copy: xsender core's FakeSunat
 * routes by path and isn't published as a test-jar.
 */
public class FakeSunat implements AutoCloseable {

    /** The ticket of {@code sunat/bill_service_response_ticket.xml} */
    public static final String TICKET = "1703154974517";

    private final HttpServer server;

    public FakeSunat() {
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        server.createContext("/", exchange -> {
            exchange.getRequestBody().readAllBytes();
            String answer = switch (String.valueOf(exchange.getRequestHeaders().getFirst("SOAPAction"))) {
                case "urn:sendBill" -> "/sunat/bill_service_response_ok.xml";
                case "urn:sendSummary" -> "/sunat/bill_service_response_ticket.xml";
                default -> null;
            };
            if (answer == null) {
                exchange.sendResponseHeaders(500, -1);
                exchange.close();
                return;
            }
            byte[] body = resource(answer);
            exchange.getResponseHeaders().set("Content-Type", "text/xml");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
    }

    /** The URL of its billService */
    public String billServiceUrl() {
        return "http://127.0.0.1:" + server.getAddress().getPort() + "/billService";
    }

    private static byte[] resource(String name) {
        try (InputStream is = FakeSunat.class.getResourceAsStream(name)) {
            if (is == null) {
                throw new IllegalArgumentException("Missing test resource " + name);
            }
            return is.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
