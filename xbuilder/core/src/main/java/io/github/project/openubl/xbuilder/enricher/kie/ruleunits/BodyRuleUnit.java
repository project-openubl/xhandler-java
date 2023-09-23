/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xbuilder.enricher.kie.ruleunits;

import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.enricher.kie.RuleFactory;
import io.github.project.openubl.xbuilder.enricher.kie.RuleFactoryRegistry;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import io.github.project.openubl.xbuilder.enricher.kie.RuleUnit;

import java.util.List;

public class BodyRuleUnit implements RuleUnit {

    private final Defaults defaults;
    private final BodyRuleContext context;
    private final List<RuleFactory> rules;

    public BodyRuleUnit(RulePhase.PhaseType phaseType, Defaults defaults, BodyRuleContext context) {
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
