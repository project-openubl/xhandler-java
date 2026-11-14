package io.github.project.openubl.quarkus.xbuilder.runtime;

import io.github.project.openubl.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.models.Defaults;
import io.quarkus.arc.DefaultBean;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * Provides an {@link XBuilder} whose defaults use the rates configured in
 * {@code quarkus.xbuilder.*}. It is a default bean: an application bean of
 * the same type replaces it.
 */
@Singleton
public class XBuilderProducer {

    @Produces
    @Singleton
    @DefaultBean
    XBuilder xbuilder(XBuilderConfig config) {
        return new XBuilder(() -> {
            Defaults defaults = XBuilder.todayDefaults();
            config.igvTasa().ifPresent(defaults::setIgvTasa);
            config.icbTasa().ifPresent(defaults::setIcbTasa);
            config.ivapTasa().ifPresent(defaults::setIvapTasa);
            return defaults;
        });
    }
}
