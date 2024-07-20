package io.github.project.openubl.xbuilder.enricher.kie.ruleunits;

import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.enricher.kie.RuleFactory;
import io.github.project.openubl.xbuilder.enricher.kie.RuleFactoryRegistry;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import io.github.project.openubl.xbuilder.enricher.kie.RuleUnit;

import java.util.List;

public class HeaderRuleUnit implements RuleUnit {

    private final Defaults defaults;
    private final HeaderRuleContext context;
    private final List<RuleFactory> rules;

    public HeaderRuleUnit(RulePhase.PhaseType phaseType, Defaults defaults, HeaderRuleContext context) {
        this.defaults = defaults;
        this.context = context;
        this.rules = RuleFactoryRegistry.getRuleFactories(phaseType);
    }

    @Override
    public void modify(Object object) {
        int prevHashCode;
        int currentHashCode;

        do {
            prevHashCode = object.hashCode();
            rules
                    .stream()
                    .filter(factory -> factory.test(object))
                    .map(factory -> factory.create(defaults, context))
                    .forEach(rule -> rule.modify(object));
            currentHashCode = object.hashCode();
        } while (prevHashCode != currentHashCode);
    }
}
