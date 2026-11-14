package io.github.project.openubl.xhandler.core;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Reads the corpus that every xhandler binding must pass
 * ({@code testdata/conformance}, synced from xhandler-rust).
 */
public final class Conformance {

    /**
     * One corpus file: {@code <op>/<name>.json}.
     */
    public record Case(String op, String name, JsonNode input, JsonNode expected) {
        public boolean expectsError() {
            return expected.has("error");
        }

        @Override
        public String toString() {
            return op + "/" + name;
        }
    }

    private Conformance() {
    }

    /**
     * @param op only the cases of this op, or all when {@code null}
     */
    public static List<Case> cases(String op) {
        Path root = corpus();
        try (Stream<Path> files = Files.walk(root)) {
            List<Case> cases = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .sorted()
                    .map(path -> read(root, path))
                    .filter(c -> op == null || c.op().equals(op))
                    .toList();
            if (cases.isEmpty()) {
                throw new IllegalStateException("No conformance cases in " + root + " (run make sync)");
            }
            return cases;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** The corpus is at the repository root, above the module being tested. */
    private static Path corpus() {
        for (Path dir = Path.of("").toAbsolutePath(); dir != null; dir = dir.getParent()) {
            Path candidate = dir.resolve("testdata/conformance");
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("testdata/conformance not found (run make sync)");
    }

    private static Case read(Path root, Path path) {
        String relative = root.relativize(path).toString().replace('\\', '/');
        int slash = relative.indexOf('/');
        try {
            JsonNode json = Json.MAPPER.readTree(path.toFile());
            return new Case(
                    relative.substring(0, slash),
                    relative.substring(slash + 1, relative.length() - ".json".length()),
                    json.get("input"),
                    json.get("expected")
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
