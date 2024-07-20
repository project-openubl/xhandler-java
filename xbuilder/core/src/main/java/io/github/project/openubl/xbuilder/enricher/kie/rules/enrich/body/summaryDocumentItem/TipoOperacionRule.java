package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.summaryDocumentItem;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog19;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocumentsItem;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSummaryDocumentsItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSummaryDocumentsItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoOperacionRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (
                isSummaryDocumentsItem.test(object) &&
                        whenSummaryDocumentsItem.apply(object).map(item -> item.getTipoOperacion() != null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<SummaryDocumentsItem> consumer = item -> {
            Catalog.valueOfCode(Catalog19.class, item.getTipoOperacion())
                    .ifPresent(catalog -> item.setTipoOperacion(catalog.getCode()));
        };
        whenSummaryDocumentsItem.apply(object).ifPresent(consumer);
    }
}
