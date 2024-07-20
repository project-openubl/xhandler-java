package io.github.project.openubl.xbuilder.enricher.kie;

import io.github.project.openubl.xbuilder.enricher.config.Defaults;

public interface RuleFactory {
    boolean test(Object object);

    Rule create(Defaults defaults, RuleContext ruleContext);
}
