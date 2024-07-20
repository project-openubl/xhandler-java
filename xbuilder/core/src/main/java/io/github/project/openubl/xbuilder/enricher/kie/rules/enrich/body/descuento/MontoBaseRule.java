package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.descuento;

import io.github.project.openubl.xbuilder.content.models.standard.general.Descuento;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDescuento;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDescuento;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class MontoBaseRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return isDescuento.test(object) && whenDescuento.apply(object)
                .map(descuento -> descuento.getMontoBase() == null && descuento.getMonto() != null)
                .orElse(false);
    }

    @Override
    public void modify(Object object) {
        Consumer<Descuento> consumer = descuento -> {
            descuento.setMontoBase(descuento.getMonto());
        };
        whenDescuento.apply(object).ifPresent(consumer);
    }
}
