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
        return new XBuilder(Defaults.builder()
                .igvTasa(config.igvTasa().orElse(null))
                .icbTasa(config.icbTasa().orElse(null))
                .ivapTasa(config.ivapTasa().orElse(null))
                .build());
    }
}
