package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.detalle;

import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TasaIcbRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem.apply(object)
                .map(documento -> documento.getTasaIcb() == null)
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            BigDecimal tasaIcb = getRuleContext().getTasaIcb();
            detalle.setTasaIcb(tasaIcb);
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
