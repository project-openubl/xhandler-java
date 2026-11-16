package io.github.project.openubl.quarkus.xsender.deployment;

import io.github.project.openubl.quarkus.xsender.runtime.XSenderProducer;
import io.github.project.openubl.xsender.models.answers.CDR;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

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

    @BuildStep
    IndexDependencyBuildItem indexAnswers() {
        return new IndexDependencyBuildItem("io.github.project-openubl", "xsender");
    }

    /** Jackson reads the answers of the xhandler core into the generated types */
    @BuildStep
    void registerAnswers(CombinedIndexBuildItem index, BuildProducer<ReflectiveClassBuildItem> reflection) {
        String answers = CDR.class.getPackageName();
        String[] classes = index.getIndex().getKnownClasses().stream()
                .map(c -> c.name().toString())
                .filter(name -> name.startsWith(answers + "."))
                .toArray(String[]::new);
        reflection.produce(ReflectiveClassBuildItem.builder(classes).methods().fields().build());
    }

    /** SUNAT is called over HTTPS */
    @BuildStep
    ExtensionSslNativeSupportBuildItem sslNativeSupport() {
        return new ExtensionSslNativeSupportBuildItem(FEATURE);
    }
}
