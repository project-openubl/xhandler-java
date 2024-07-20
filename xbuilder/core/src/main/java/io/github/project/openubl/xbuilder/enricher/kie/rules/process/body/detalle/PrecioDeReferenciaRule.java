package io.github.project.openubl.xbuilder.enricher.kie.rules.process.body.detalle;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.PROCESS)
public class PrecioDeReferenciaRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem.apply(object)
                .map(documento -> documento.getPrecioReferencia() == null &&
                        documento.getPrecio() != null &&
                        documento.getTasaIgv() != null &&
                        documento.getIgvTipo() != null &&
                        documento.getTasaIsc() != null
                )
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            Catalog7 catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);

            BigDecimal precioReferencia;
            if (catalog7.isOperacionOnerosa()) {
                if (detalle.isPrecioConImpuestos()) {
                    precioReferencia = detalle.getPrecio();
                } else {
                    precioReferencia = detalle.getPrecio()
                            .multiply(detalle.getTasaIgv().add(BigDecimal.ONE))
                            .multiply(detalle.getTasaIsc().add(BigDecimal.ONE));
                }
            } else {
                precioReferencia = detalle.getPrecio();
            }

            detalle.setPrecioReferencia(precioReferencia);
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
