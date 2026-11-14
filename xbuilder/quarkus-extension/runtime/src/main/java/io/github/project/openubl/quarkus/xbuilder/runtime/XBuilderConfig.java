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
     * IGV rate of the documents that do not set it; the current SUNAT rate by default
     */
    Optional<BigDecimal> igvTasa();

    /**
     * ICB rate of the documents that do not set it; the current SUNAT rate by default
     */
    Optional<BigDecimal> icbTasa();

    /**
     * IVAP rate of the documents that do not set it; the current SUNAT rate by default
     */
    Optional<BigDecimal> ivapTasa();
}
