package io.github.project.openubl.xbuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.project.openubl.xbuilder.models.Defaults;
import io.github.project.openubl.xbuilder.models.Document;
import io.github.project.openubl.xhandler.core.Json;
import io.github.project.openubl.xhandler.core.Xhandler;
import io.github.project.openubl.xhandler.core.XhandlerException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Creates the UBL XML of SUNAT documents: invoices (boletas and facturas),
 * credit and debit notes, guias de remision, perceptions, retentions,
 * summaries, voided documents and reversions.
 * <p>
 * Only the essential data is required: the xhandler core fills the defaults
 * (currency, tax rates, document types...) and computes taxes and totals
 * before rendering the XML.
 *
 * <pre>{@code
 * Invoice invoice = Invoice.builder()
 *         .serieNumero("F001-1")
 *         .proveedor(proveedor)
 *         .cliente(cliente)
 *         .detalles(List.of(detalle))
 *         .build();
 * String xml = new XBuilder().build(invoice);
 * }</pre>
 * <p>
 * Thread-safe. Every method throws {@link XhandlerException} when the
 * document is invalid, e.g. with an unknown catalog code.
 */
public class XBuilder {

    private static final ZoneId PERU = ZoneId.of("America/Lima");

    private final Supplier<Defaults> defaults;

    /**
     * Uses {@link #todayDefaults()} for the documents built without defaults.
     */
    public XBuilder() {
        this(XBuilder::todayDefaults);
    }

    /**
     * @param defaults the defaults of the documents built without them, e.g.
     *                 with other tax rates
     */
    public XBuilder(Supplier<Defaults> defaults) {
        this.defaults = defaults;
    }

    /**
     * @return the defaults for documents issued today in Peru, with the current SUNAT tax rates
     */
    public static Defaults todayDefaults() {
        return Defaults.builder().fechaEmision(LocalDate.now(PERU)).build();
    }

    /**
     * Enriches the document with the defaults of this builder and renders its XML.
     */
    public String build(Document document) {
        return build(document, defaults.get());
    }

    /**
     * Enriches the document and renders its XML. The document itself is not
     * modified; use {@link #enrich(Document, Defaults)} to get the computed values.
     */
    public String build(Document document, Defaults defaults) {
        return Xhandler.invoke("build", input(document, defaults)).asText();
    }

    /**
     * Same as {@link #enrich(Document, Defaults)} with the defaults of this builder.
     */
    public <T extends Document> T enrich(T document) {
        return enrich(document, defaults.get());
    }

    /**
     * @return a copy of the document with its defaults filled in and its
     * taxes and totals computed, as {@link #build(Document, Defaults)} renders it
     */
    @SuppressWarnings("unchecked")
    public <T extends Document> T enrich(T document, Defaults defaults) {
        var enriched = Xhandler.invoke("enrich", input(document, defaults));
        try {
            return (T) Json.MAPPER.treeToValue(enriched.get("spec"), document.getClass());
        } catch (JsonProcessingException e) {
            throw new XhandlerException("xbuilder: " + e.getMessage(), e);
        }
    }

    private static Map<String, Object> input(Document document, Defaults defaults) {
        return Map.of(
                "document", Map.of("kind", document.kind(), "spec", document),
                "defaults", defaults
        );
    }
}
