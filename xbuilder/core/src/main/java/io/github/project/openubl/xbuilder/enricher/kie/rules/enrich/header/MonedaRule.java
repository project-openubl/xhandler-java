package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header;

import io.github.project.openubl.xbuilder.content.models.common.Document;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDocument;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDocument;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class MonedaRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isDocument.test(object) &&
                        whenDocument.apply(object).map(documento -> documento.getMoneda() == null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Document> consumer = document -> document.setMoneda("PEN");
        whenDocument.apply(object).ifPresent(consumer);
    }
}
