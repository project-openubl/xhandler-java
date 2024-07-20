package io.github.project.openubl.xbuilder.enricher.kie;

import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.enricher.kie.ruleunits.BodyRuleContext;

public abstract class AbstractBodyRule implements RuleFactory, Rule {

    private Defaults defaults;
    private BodyRuleContext ruleContext;

    @Override
    public Rule create(Defaults defaults, RuleContext ruleContext) {
        this.defaults = defaults;
        if (ruleContext instanceof BodyRuleContext) {
            this.ruleContext = (BodyRuleContext) ruleContext;
        } else {
            throw new IllegalStateException("HeaderRule requires HeaderContext");
        }
        return this;
    }

    public BodyRuleContext getRuleContext() {
        return ruleContext;
    }
}
