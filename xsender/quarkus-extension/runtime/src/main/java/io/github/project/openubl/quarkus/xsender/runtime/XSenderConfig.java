package io.github.project.openubl.quarkus.xsender.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "xsender", phase = ConfigPhase.RUN_TIME)
public class XSenderConfig {

    /**
     * Enable logging feature
     */
    @ConfigItem
    public boolean enableLoggingFeature;
}
