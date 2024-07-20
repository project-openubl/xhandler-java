package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.summaryDocumentItem;

import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocumentsItem;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSummaryDocumentsItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSummaryDocumentsItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class MonedaRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSummaryDocumentsItem.test(object) && whenSummaryDocumentsItem.apply(object)
                .map(item -> item.getComprobante() != null &&
                        item.getComprobante().getMoneda() == null
                )
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<SummaryDocumentsItem> consumer = item -> {
            String moneda = getRuleContext().getMoneda();
            item.getComprobante().setMoneda(moneda);
        };
        whenSummaryDocumentsItem.apply(object).ifPresent(consumer);
    }
}
