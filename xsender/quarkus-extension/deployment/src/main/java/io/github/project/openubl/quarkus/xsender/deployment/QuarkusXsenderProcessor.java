package io.github.project.openubl.quarkus.xsender.deployment;

import io.github.project.openubl.quarkus.xsender.runtime.XSenderProducer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusXsenderProcessor {

    private static final String FEATURE = "quarkus-xsender";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem producer() {
        return AdditionalBeanBuildItem.unremovableOf(XSenderProducer.class);
    }

    /** SUNAT is called over HTTPS */
    @BuildStep
    ExtensionSslNativeSupportBuildItem sslNativeSupport() {
        return new ExtensionSslNativeSupportBuildItem(FEATURE);
    }
}
