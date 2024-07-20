package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header;

import io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocument;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocument;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TasaIcbRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isSalesDocument.test(object) &&
                        whenSalesDocument.apply(object).map(documento -> documento.getTasaIcb() == null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<SalesDocument> consumer = document -> document.setTasaIcb(getDefaults().getIcbTasa());
        whenSalesDocument.apply(object).ifPresent(consumer);
    }
}
