package io.github.project.openubl.xbuilder.enricher.kie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RulePhase {
    PhaseType type();

    int priority() default 0;

    enum PhaseType {
        ENRICH,
        PROCESS,
        SUMMARY,
    }
}
