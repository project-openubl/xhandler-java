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
public class IgvBaseImponibleRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem.apply(object)
                .map(documento -> documento.getIgvBaseImponible() == null &&
                        documento.getIgvTipo() != null &&
                        documento.getCantidad() != null &&
                        documento.getPrecio() != null &&
                        documento.getPrecioReferencia() != null &&
                        documento.getIsc() != null
                )
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            Catalog7 catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);

            BigDecimal baseImponible;
            if (catalog7.isOperacionOnerosa()) {
                baseImponible = detalle.getCantidad().multiply(detalle.getPrecio());
            } else {
                baseImponible = detalle.getCantidad().multiply(detalle.getPrecioReferencia());
            }
            detalle.setIgvBaseImponible(baseImponible.add(detalle.getIsc()));
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
