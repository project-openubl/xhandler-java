package io.github.project.openubl.xhandler.core;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import com.dylibso.chicory.wasm.WasmModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Runs the operations of the xhandler WebAssembly core, compiled to JVM
 * bytecode at build time ({@link XhandlerModule}).
 * <p>
 * The core exports one JSON entry point (see xhandler-rust/xhandler-wasm):
 * the request is {@code {"op", "input"}} and the response {@code {"ok": ...}}
 * or {@code {"error": {"kind", "message"}}}.
 */
public final class Xhandler {

    /** Version of the embedded xhandler core. */
    public static final String CORE_VERSION = readVersion();

    private static final WasmModule MODULE = XhandlerModule.load();

    /**
     * Idle instances: an instance runs one call at a time and parses the XML
     * templates on its first use, so it is worth reusing.
     */
    private static final BlockingQueue<Module> IDLE =
            new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors());

    private Xhandler() {
    }

    /**
     * Runs {@code op} with {@code input} (mapped to JSON with {@link Json#MAPPER}).
     *
     * @return the result of the operation
     * @throws XhandlerException when the operation fails
     */
    public static JsonNode invoke(String op, Object input) {
        ObjectNode request = Json.MAPPER.createObjectNode().put("op", op);
        request.set("input", Json.MAPPER.valueToTree(input));

        JsonNode response;
        try {
            response = Json.MAPPER.readTree(invokeRaw(Json.MAPPER.writeValueAsBytes(request)));
        } catch (IOException e) {
            throw new XhandlerException("xhandler " + op + ": " + e.getMessage(), e);
        }

        JsonNode error = response.get("error");
        if (error != null) {
            ErrorKind kind = Json.MAPPER.convertValue(error.get("kind"), ErrorKind.class);
            throw new XhandlerException(kind, error.get("message").asText());
        }
        return response.get("ok");
    }

    /**
     * Sends a JSON request to the core and returns its JSON response.
     */
    public static byte[] invokeRaw(byte[] request) {
        Module module = IDLE.poll();
        if (module == null) {
            module = new Module();
        }
        byte[] response;
        try {
            response = module.invoke(request);
        } catch (RuntimeException trap) {
            // e.g. a panic in the core: the instance is unusable
            module.close();
            throw new XhandlerException("xhandler core: " + trap.getMessage(), trap);
        }
        if (!IDLE.offer(module)) {
            module.close();
        }
        return response;
    }

    private static String readVersion() {
        try (InputStream in = Xhandler.class.getResourceAsStream("VERSION")) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final class Module implements AutoCloseable {
        private final WasiPreview1 wasi;
        private final Instance instance;
        private final ExportFunction alloc;
        private final ExportFunction free;
        private final ExportFunction call;

        Module() {
            wasi = WasiPreview1.builder().withOptions(WasiOptions.builder().build()).build();
            instance = Instance.builder(MODULE)
                    .withImportValues(ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
                    .withMachineFactory(XhandlerModule::create)
                    .withStart(false)
                    .build();
            instance.export("_initialize").apply();
            alloc = instance.export("xh_alloc");
            free = instance.export("xh_free");
            call = instance.export("xh_invoke");
        }

        /** Runs one request. After a trap the instance is discarded, so nothing is freed then. */
        byte[] invoke(byte[] request) {
            long ptr = alloc.apply(request.length)[0];
            instance.memory().write((int) ptr, request);
            long packed = call.apply(ptr, request.length)[0];
            int responsePtr = (int) (packed >>> 32);
            int responseLength = (int) packed;
            byte[] response = instance.memory().readBytes(responsePtr, responseLength);
            free.apply(ptr, request.length);
            free.apply(responsePtr, responseLength);
            return response;
        }

        @Override
        public void close() {
            wasi.close();
        }
    }
}
