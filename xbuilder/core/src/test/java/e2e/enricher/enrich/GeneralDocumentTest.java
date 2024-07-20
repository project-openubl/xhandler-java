package e2e.enricher.enrich;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.CatalogContadoCredito;
import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.CuotaDePago;
import io.github.project.openubl.xbuilder.content.models.standard.general.FormaDePago;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneralDocumentTest extends AbstractTest {

    @Test
    public void testEnrichMoneda() {
        // Given
        Invoice input = Invoice.builder().build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals("PEN", input.getMoneda());
    }

    @Test
    public void testDontEnrichMoneda() {
        // Given
        Invoice input = Invoice.builder().moneda("USD").build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals("USD", input.getMoneda());
    }

    @Test
    public void testEnrichFechaEmision() {
        // Given
        Invoice input = Invoice.builder().build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals(dateProvider.now(), input.getFechaEmision());
    }

    @Test
    public void testDontEnrichFechaEmision() {
        // Given
        LocalDate fechaEmision = LocalDate.of(1991, 1, 30);
        Invoice input = Invoice.builder().fechaEmision(fechaEmision).build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals(fechaEmision, input.getFechaEmision());
    }

    @Test
    public void testEnrichFormaPagoTipo_Contado() {
        // Given
        Invoice input = Invoice.builder().build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals(CatalogContadoCredito.CONTADO.getCode(), input.getFormaDePago().getTipo());
    }

    @Test
    public void testEnrichFormaPagoTipo_Credito() {
        // Given
        Invoice input = Invoice.builder()
                .formaDePago(
                        FormaDePago
                                .builder()
                                .cuota(CuotaDePago.builder().fechaPago(LocalDate.now()).importe(BigDecimal.TEN).build())
                                .build()
                )
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals(CatalogContadoCredito.CREDITO.getCode(), input.getFormaDePago().getTipo());
    }

    @Test
    public void testEnrichFormaPagoTipo_CorrectWrongUserDefinedValue() {
        // Given

        // Invoice with no "cuotas" has "formaDePago" CREDITO. It must be corrected
        Invoice input1 = Invoice.builder()
                .formaDePago(FormaDePago.builder().tipo(CatalogContadoCredito.CREDITO.getCode()).build())
                .build();

        // Invoice with "cuotas" has "formaDePago" CONTADO. It must be corrected
        Invoice input2 = Invoice.builder()
                .formaDePago(
                        FormaDePago
                                .builder()
                                .tipo(CatalogContadoCredito.CONTADO.getCode())
                                .cuota(CuotaDePago.builder().fechaPago(LocalDate.now()).importe(BigDecimal.TEN).build())
                                .build()
                )
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input1);
        enricher.enrich(input2);

        // Then
        assertEquals(CatalogContadoCredito.CONTADO.getCode(), input1.getFormaDePago().getTipo());
        assertEquals(CatalogContadoCredito.CREDITO.getCode(), input2.getFormaDePago().getTipo());
    }

    @Test
    public void testEnrichFirmante() {
        // Given
        Invoice input = Invoice.builder()
                .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Mi razón social").build())
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals(input.getProveedor().getRuc(), input.getFirmante().getRuc());
        assertEquals(input.getProveedor().getRazonSocial(), input.getFirmante().getRazonSocial());
    }

    @Test
    public void testEnrichFirmante_whenFirmanteIsPartiallyFilled() {
        // Given
        Invoice input = Invoice.builder()
                .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Mi razón social").build())
                .firmante(Firmante.builder().ruc("12345678912").build())
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals(input.getProveedor().getRuc(), input.getFirmante().getRuc());
        assertEquals(input.getProveedor().getRazonSocial(), input.getFirmante().getRazonSocial());
    }

    @Test
    public void testEnrichProveedorDireccionCodigoLocal() {
        // Given
        Invoice input = Invoice.builder().proveedor(Proveedor.builder().build()).build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        assertEquals("0000", input.getProveedor().getDireccion().getCodigoLocal());
    }
}
