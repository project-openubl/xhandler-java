package io.github.project.openubl.xbuilder;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xbuilder.models.Cliente;
import io.github.project.openubl.xbuilder.models.Defaults;
import io.github.project.openubl.xbuilder.models.Detalle;
import io.github.project.openubl.xbuilder.models.Document;
import io.github.project.openubl.xbuilder.models.Invoice;
import io.github.project.openubl.xbuilder.models.Proveedor;
import io.github.project.openubl.xhandler.core.Conformance;
import io.github.project.openubl.xhandler.core.ErrorKind;
import io.github.project.openubl.xhandler.core.Json;
import io.github.project.openubl.xhandler.core.XhandlerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XBuilderTest {

    private final XBuilder xbuilder = new XBuilder();

    static List<Conformance.Case> buildCases() {
        return Conformance.cases("build").stream().filter(c -> !c.expectsError()).toList();
    }

    static List<Conformance.Case> enrichCases() {
        return Conformance.cases("enrich");
    }

    /**
     * Decodes a corpus document into its model; unknown properties fail, so a
     * model change in Rust that was not synced breaks the tests.
     */
    static Document documentOf(JsonNode tagged) throws Exception {
        Class<?> model = Class.forName(Document.class.getPackageName() + "." + tagged.get("kind").asText());
        return (Document) Json.MAPPER.treeToValue(tagged.get("spec"), model);
    }

    static Defaults defaultsOf(Conformance.Case c) throws Exception {
        return Json.MAPPER.treeToValue(c.input().get("defaults"), Defaults.class);
    }

    @ParameterizedTest
    @MethodSource("buildCases")
    void buildCorpusThroughModels(Conformance.Case c) throws Exception {
        Document document = documentOf(c.input().get("document"));
        assertEquals(c.expected().get("ok").asText(), xbuilder.build(document, defaultsOf(c)));
    }

    @ParameterizedTest
    @MethodSource("enrichCases")
    void enrichCorpusThroughModels(Conformance.Case c) throws Exception {
        Document document = documentOf(c.input().get("document"));
        Document expected = documentOf(c.expected().get("ok"));
        assertEquals(expected, xbuilder.enrich(document, defaultsOf(c)));
    }

    static Invoice invoice() {
        return Invoice.builder()
                .serieNumero("F001-1")
                .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
                .cliente(Cliente.builder()
                        .tipoDocumentoIdentidad("6")
                        .numeroDocumentoIdentidad("12121212121")
                        .nombre("Carlos Feria")
                        .build())
                .detalles(List.of(Detalle.builder()
                        .descripcion("Item")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .build()))
                .build();
    }

    @Test
    void enrichComputesTotals() {
        Defaults defaults = Defaults.builder().fechaEmision(LocalDate.of(2019, 12, 24)).build();
        Invoice enriched = xbuilder.enrich(invoice(), defaults);
        assertEquals(0, new BigDecimal("236").compareTo(enriched.getTotalImporte().getImporte()));
        assertEquals("PEN", enriched.getMoneda());
        assertEquals(LocalDate.of(2019, 12, 24), enriched.getFechaEmision());
    }

    @Test
    void buildReportsValidationErrors() {
        Invoice document = invoice();
        document.getDetalles().get(0).setIgvTipo("XX");
        XhandlerException error = assertThrows(XhandlerException.class, () -> xbuilder.build(document));
        assertEquals(ErrorKind.VALIDATION, error.getKind());
    }
}
