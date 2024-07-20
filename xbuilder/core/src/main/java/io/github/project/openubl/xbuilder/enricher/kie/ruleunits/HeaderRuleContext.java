package io.github.project.openubl.xbuilder.enricher.kie.ruleunits;

import io.github.project.openubl.xbuilder.enricher.kie.RuleContext;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class HeaderRuleContext implements RuleContext {

    private LocalDate localDate;
}
