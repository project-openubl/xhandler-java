package io.github.project.openubl.xbuilder.enricher.kie;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class RuleFactoryRegistry {

    public static List<RuleFactory> getRuleFactories(RulePhase.PhaseType phaseType) {
        return ServiceLoader
                .load(RuleFactory.class)
                .stream()
                .map(ruleProvider -> {
                    RulePhase rulePhase = ruleProvider.type().getAnnotation(RulePhase.class);
                    return Map.entry(ruleProvider, rulePhase);
                })
                .filter(ruleProviderPhaseEntry -> {
                    RulePhase rulePhase = ruleProviderPhaseEntry.getValue();
                    return rulePhase.type().equals(phaseType);
                })
                .sorted((o1, o2) -> o2.getValue().priority() - o1.getValue().priority())
                .map(ruleProviderPhaseEntry -> ruleProviderPhaseEntry.getKey().get())
                .collect(Collectors.toList());
    }
}
