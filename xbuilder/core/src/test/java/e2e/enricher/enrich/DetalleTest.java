package e2e.enricher.enrich;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog16;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetalleTest extends AbstractTest {

    @Test
    public void testEnrichUnidadMedida() {
        // Given
        Invoice input = Invoice.builder().detalle(DocumentoVentaDetalle.builder().build()).build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        input
                .getDetalles()
                .forEach(detalle -> {
                    assertEquals("NIU", detalle.getUnidadMedida());
                });
    }

    @Test
    public void testDontEnrichUnidadMedida() {
        // Given
        Invoice input = Invoice.builder().detalle(DocumentoVentaDetalle.builder().unidadMedida("KG").build()).build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        input
                .getDetalles()
                .forEach(detalle -> {
                    assertEquals("KG", detalle.getUnidadMedida());
                });
    }

    @Test
    public void testEnrichIgvTipo() {
        // Given
        Invoice input = Invoice.builder().detalle(DocumentoVentaDetalle.builder().build()).build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        input
                .getDetalles()
                .forEach(detalle -> {
                    assertEquals(Catalog7.GRAVADO_OPERACION_ONEROSA.getCode(), detalle.getIgvTipo());
                });
    }

    @Test
    public void testDontEnrichIgvTipo() {
        // Given
        Invoice input = Invoice.builder()
                .detalle(DocumentoVentaDetalle.builder().igvTipo(Catalog7.INAFECTO_RETIRO.getCode()).build())
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        input
                .getDetalles()
                .forEach(detalle -> {
                    assertEquals(Catalog7.INAFECTO_RETIRO.getCode(), detalle.getIgvTipo());
                });
    }

    @Test
    public void testEnrichPrecioReferenciaTipo_PrecioConIgv() {
        // Given
        Invoice input = Invoice.builder()
                .detalle(DocumentoVentaDetalle.builder().igvTipo(Catalog7.GRAVADO_OPERACION_ONEROSA.getCode()).build())
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        input
                .getDetalles()
                .forEach(detalle -> {
                    assertEquals(Catalog16.PRECIO_UNITARIO_INCLUYE_IGV.getCode(), detalle.getPrecioReferenciaTipo());
                });
    }

    @Test
    public void testEnrichPrecioReferenciaTipo_ValorReferencial() {
        // Given
        Invoice input = Invoice.builder()
                .detalle(DocumentoVentaDetalle.builder()
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_DONACION.getCode())
                        .build()
                )
                .build();

        // When
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // Then
        input
                .getDetalles()
                .forEach(detalle -> {
                    assertEquals(
                            Catalog16.VALOR_REFERENCIAL_UNITARIO_EN_OPERACIONES_NO_ONEROSAS.getCode(),
                            detalle.getPrecioReferenciaTipo()
                    );
                });
    }
}
