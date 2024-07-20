package io.github.project.openubl.quarkus.xbuilder.runtime;

import io.github.project.openubl.quarkus.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.renderer.EngineProducer;
import io.quarkus.qute.Engine;
import io.quarkus.qute.EngineBuilder;
import io.quarkus.qute.HtmlEscaper;
import io.quarkus.qute.Template;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

@Singleton
public class DefaultXBuilder implements XBuilder {

    @Inject
    Engine engine;

    @Inject
    XBuilderConfig config;

    void configureEngine(@Observes EngineBuilder builder) {
        builder.addResultMapper(
                new HtmlEscaper(List.of("text/html", "text/xml", "application/xml", "application/xhtml+xml"))
        );

        EngineProducer.getInstance().getEngine().getValueResolvers().forEach(builder::addValueResolver);
    }

    @Override
    public Template getTemplate(Type type) {
        return engine.getTemplate(CustomTemplateLocator.PREFIX + type.getTemplatePath());
    }

    @Override
    public Defaults getDefaults() {
        return Defaults.builder()
                .igvTasa(config.igvTasa.orElse(new BigDecimal("0.18")))
                .icbTasa(config.icbTasa.orElse(new BigDecimal("0.2")))
                .build();
    }
}
