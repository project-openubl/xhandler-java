package io.github.project.openubl.xbuilder.enricher.kie.rules.process.body.detalle;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.PROCESS)
public class PrecioRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem.apply(object)
                .map(documento -> documento.getPrecioReferencia() != null && documento.getIgvTipo() != null)
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            Catalog7 catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);

            BigDecimal precio;
            if (catalog7.isOperacionOnerosa()) {
                if (detalle.isPrecioConImpuestos()) {
                    precio = detalle.getPrecioReferencia()
                            .divide(detalle.getTasaIgv().add(BigDecimal.ONE), 10, RoundingMode.HALF_EVEN)
                            .divide(detalle.getTasaIsc().add(BigDecimal.ONE), 10, RoundingMode.HALF_EVEN);
                } else {
                    precio = detalle.getPrecio();
                }
            } else {
                precio = BigDecimal.ZERO;
            }

            detalle.setPrecio(precio);
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
