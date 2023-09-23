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
