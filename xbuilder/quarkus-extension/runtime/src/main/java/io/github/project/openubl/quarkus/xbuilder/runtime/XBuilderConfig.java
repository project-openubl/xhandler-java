package io.github.project.openubl.quarkus.xbuilder.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.math.BigDecimal;
import java.util.Optional;

@ConfigMapping(prefix = "quarkus.xbuilder")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface XBuilderConfig {

    /**
     * Default igvTasa
     */
    Optional<BigDecimal> igvTasa();

    /**
     * Default icbTasa
     */
    Optional<BigDecimal> icbTasa();
}
