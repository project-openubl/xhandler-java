package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.descuento;

import io.github.project.openubl.xbuilder.content.models.standard.general.Descuento;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDescuento;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDescuento;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class FactorRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return isDescuento.test(object) && whenDescuento.apply(object)
                .map(descuento -> descuento.getFactor() == null)
                .orElse(false);
    }

    @Override
    public void modify(Object object) {
        Consumer<Descuento> consumer = descuento -> {
            descuento.setFactor(BigDecimal.ONE);
        };
        whenDescuento.apply(object).ifPresent(consumer);
    }
}
