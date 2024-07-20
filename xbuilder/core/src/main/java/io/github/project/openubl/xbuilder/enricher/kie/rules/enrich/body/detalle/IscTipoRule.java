package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.detalle;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog8;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class IscTipoRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return isSalesDocumentItem.test(object);
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            Catalog8 catalog8;
            if (detalle.getIscTipo() == null) {
                catalog8 = Catalog8.SISTEMA_AL_VALOR;
            } else {
                catalog8 = Catalog.valueOfCode(Catalog8.class, detalle.getIscTipo()).orElseThrow(Catalog.invalidCatalogValue);
            }

            detalle.setIscTipo(catalog8.getCode());
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
