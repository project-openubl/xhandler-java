package io.github.project.openubl.quarkus.xsender.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.xsender")
public interface XSenderConfig {

    /**
     * Enable logging feature
     */
    @WithDefault("false")
    boolean enableLoggingFeature();
}
