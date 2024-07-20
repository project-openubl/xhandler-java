package io.github.project.openubl.xbuilder.enricher.kie;

import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.enricher.kie.ruleunits.HeaderRuleContext;

public abstract class AbstractHeaderRule implements RuleFactory, Rule {

    private Defaults defaults;
    private HeaderRuleContext ruleContext;

    @Override
    public Rule create(Defaults defaults, RuleContext ruleContext) {
        this.defaults = defaults;
        if (ruleContext instanceof HeaderRuleContext) {
            this.ruleContext = (HeaderRuleContext) ruleContext;
        } else {
            throw new IllegalStateException("HeaderRule requires HeaderContext");
        }
        return this;
    }

    public Defaults getDefaults() {
        return defaults;
    }

    public HeaderRuleContext getRuleContext() {
        return ruleContext;
    }
}
