package io.github.project.openubl.xhandler.core;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XhandlerTest {

    static List<Conformance.Case> cases() {
        return Conformance.cases(null);
    }

    @ParameterizedTest
    @MethodSource("cases")
    void conformance(Conformance.Case c) throws IOException {
        byte[] request = Json.MAPPER.writeValueAsBytes(Map.of("op", c.op(), "input", c.input()));
        JsonNode response = Json.MAPPER.readTree(Xhandler.invokeRaw(request));

        if (c.expectsError()) {
            assertEquals(c.expected().at("/error/kind"), response.at("/error/kind"), response::toString);
        } else {
            assertEquals(c.expected(), response);
        }
    }

    @Test
    void versionMatchesSyncedCore() throws IOException {
        String synced = Files.readString(Path.of("src/main/resources/io/github/project/openubl/xhandler/core/VERSION")).trim();
        assertEquals(synced, Xhandler.invoke("version", null).asText());
        assertEquals(synced, Xhandler.CORE_VERSION);
    }

    @Test
    void errorsAreTyped() {
        XhandlerException error = assertThrows(XhandlerException.class, () -> Xhandler.invoke("nope", null));
        assertEquals(ErrorKind.INVALID_REQUEST, error.getKind());
    }

    @Test
    void concurrentCalls() {
        Set<String> versions = ConcurrentHashMap.newKeySet();
        IntStream.range(0, 32).parallel().forEach(i -> versions.add(Xhandler.invoke("version", null).asText()));
        assertEquals(Set.of(Xhandler.CORE_VERSION), versions);
    }
}
