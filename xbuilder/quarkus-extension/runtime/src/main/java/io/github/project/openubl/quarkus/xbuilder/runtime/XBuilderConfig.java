package io.github.project.openubl.quarkus.xbuilder.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.math.BigDecimal;
import java.util.Optional;

@ConfigRoot(name = "xbuilder", phase = ConfigPhase.RUN_TIME)
public class XBuilderConfig {

    /**
     * Default igvTasa
     */
    @ConfigItem
    public Optional<BigDecimal> igvTasa;

    /**
     * Default icbTasa
     */
    @ConfigItem
    public Optional<BigDecimal> icbTasa;
}
