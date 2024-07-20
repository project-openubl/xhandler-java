package e2e.enricher.enrich;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceTest extends AbstractTest {

    @Test
    public void testEnrichTipoComprobante() {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001-1")
                .tipoComprobante(Catalog1_Invoice.BOLETA.getCode()) // This should be overwritten
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals(Catalog1_Invoice.FACTURA.getCode(), input.getTipoComprobante());
    }
}
