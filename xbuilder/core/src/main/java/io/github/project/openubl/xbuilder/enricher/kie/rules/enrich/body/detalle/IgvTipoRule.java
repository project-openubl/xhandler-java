package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.detalle;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class IgvTipoRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return isSalesDocumentItem.test(object);
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            Catalog7 catalog7;
            if (detalle.getIgvTipo() == null) {
                catalog7 = Catalog7.GRAVADO_OPERACION_ONEROSA;
            } else {
                catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);
            }

            detalle.setIgvTipo(catalog7.getCode());
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
