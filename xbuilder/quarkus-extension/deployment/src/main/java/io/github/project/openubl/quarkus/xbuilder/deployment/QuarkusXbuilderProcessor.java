package io.github.project.openubl.quarkus.xbuilder.deployment;

import io.github.project.openubl.quarkus.xbuilder.runtime.XBuilderProducer;
import io.github.project.openubl.xbuilder.models.Document;
import io.github.project.openubl.xhandler.core.ErrorKind;
import io.github.project.openubl.xhandler.core.Xhandler;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class QuarkusXbuilderProcessor {

    private static final String FEATURE = "quarkus-xbuilder";
    private static final String CORE_PATH = Xhandler.class.getPackageName().replace('.', '/');

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem producer() {
        return AdditionalBeanBuildItem.unremovableOf(XBuilderProducer.class);
    }

    @BuildStep
    IndexDependencyBuildItem indexModels() {
        return new IndexDependencyBuildItem("io.github.project-openubl", "xbuilder");
    }

    /** Jackson maps the models to and from the JSON of the xhandler core */
    @BuildStep
    void registerModels(CombinedIndexBuildItem index, BuildProducer<ReflectiveClassBuildItem> reflection) {
        String models = Document.class.getPackageName();
        String[] classes = index.getIndex().getKnownClasses().stream()
                .map(c -> c.name().toString())
                .filter(name -> name.startsWith(models + "."))
                .toArray(String[]::new);
        reflection.produce(ReflectiveClassBuildItem.builder(classes).methods().fields().build());
        reflection.produce(ReflectiveClassBuildItem.builder(ErrorKind.class).fields().build());
    }

    /** The xhandler core: the Wasm module metadata and its version */
    @BuildStep
    NativeImageResourceBuildItem coreResources() {
        return new NativeImageResourceBuildItem(CORE_PATH + "/XhandlerModule.meta", CORE_PATH + "/VERSION");
    }
}
