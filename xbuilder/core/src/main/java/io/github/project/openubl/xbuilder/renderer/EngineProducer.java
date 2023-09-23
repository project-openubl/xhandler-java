/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xbuilder.renderer;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.quarkus.qute.Engine;
import io.quarkus.qute.HtmlEscaper;
import io.quarkus.qute.RawString;
import io.quarkus.qute.ReflectionValueResolver;
import io.quarkus.qute.TemplateLocator.TemplateLocation;
import io.quarkus.qute.ValueResolver;
import io.quarkus.qute.Variant;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class EngineProducer {

    private final List<String> suffixes = List.of("qute.html", "qute.txt", "html", "txt", "xml");
    private final String basePath = "templates/";
    private final Locale defaultLocale = Locale.ENGLISH;
    private final Charset defaultCharset = StandardCharsets.UTF_8;

    private final Engine engine = Engine.builder()
            .addDefaults()
            .addLocator(this::locate)
            .removeStandaloneLines(true)
            .addResultMapper(new HtmlEscaper(List.of("text/html", "text/xml", "application/xml", "application/xhtml+xml")))
            .addValueResolver(new ReflectionValueResolver())
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(String.class)
                    .applyToName("raw")
                    .resolveSync(ctx -> {
                        String value = ((String) ctx.getBase());
                        return new RawString(value);
                    })
                    .build()
            )
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(LocalDate.class)
                    .applyToName("format")
                    .resolveSync(ctx -> {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern((String) ctx.getParams().get(0).getLiteral());
                        return ((LocalDate) ctx.getBase()).format(dtf);
                    })
                    .build()
            )
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(LocalTime.class)
                    .applyToName("format")
                    .resolveSync(ctx -> {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern((String) ctx.getParams().get(0).getLiteral());
                        return ((LocalTime) ctx.getBase()).format(dtf);
                    })
                    .build()
            )
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(BigDecimal.class)
                    .applyToName("scale")
                    .applyToParameters(1)
                    .resolveSync(ctx -> ((BigDecimal) ctx.getBase()).setScale(
                            (Integer) ctx.getParams().get(0).getLiteral(),
                            RoundingMode.HALF_EVEN
                    ))
                    .build()
            )
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(BigDecimal.class)
                    .applyToName("multiplyByInt")
                    .resolveSync(ctx -> ((BigDecimal) ctx.getBase())
                            .multiply(new BigDecimal((Integer) ctx.getParams().get(0).getLiteral()))
                    )
                    .build()
            )
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(Integer.class)
                    .applyToName("add")
                    .applyToParameters(1)
                    .resolveSync(ctx -> (Integer) ctx.getBase() + (Integer) ctx.getParams().get(0).getLiteral())
                    .build()
            )
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(Integer.class)
                    .applyToName("format")
                    .applyToParameters(1)
                    .resolveSync(ctx -> String.format((String) ctx.getParams().get(0).getLiteral(), ctx.getBase()))
                    .build()
            )
            .addValueResolver(ValueResolver.builder()
                    .applyToBaseClass(String.class)
                    .applyToName("toCatalog7")
                    .resolveSync(ctx ->
                            Catalog.valueOfCode(Catalog7.class, (String) ctx.getBase()).orElseThrow(Catalog.invalidCatalogValue)
                    )
                    .build()
            )
            .build();

    public String getBasePath() {
        return basePath;
    }

    public Engine getEngine() {
        return engine;
    }

    private Optional<TemplateLocation> locate(String path) {
        URL resource;
        String templatePath = basePath + path;

        resource = locatePath(templatePath);
        if (resource == null) {
            // Try path with suffixes
            for (String suffix : suffixes) {
                String pathWithSuffix = path + "." + suffix;
                templatePath = basePath + pathWithSuffix;
                resource = locatePath(templatePath);
                if (resource != null) {
                    break;
                }
            }
        }
        if (resource != null) {
            return Optional.of(new ResourceTemplateLocation(resource, createVariant(templatePath)));
        }
        return Optional.empty();
    }

    private URL locatePath(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = EngineProducer.class.getClassLoader();
        }
        return cl.getResource(path);
    }

    private Variant createVariant(String path) {
        // Guess the content type from the path
        String contentType = getContentType(path);
        return new Variant(defaultLocale, defaultCharset, contentType);
    }

    private String getContentType(String templatePath) {
        String fileName = templatePath;
        int slashIdx = fileName.lastIndexOf('/');
        if (slashIdx != -1) {
            fileName = fileName.substring(slashIdx, fileName.length());
        }
        int dotIdx = fileName.lastIndexOf('.');
        if (dotIdx != -1) {
            String suffix = fileName.substring(dotIdx + 1, fileName.length());
            String additionalContentType = URLConnection.getFileNameMap().getContentTypeFor(suffix);
            if (additionalContentType != null) {
                return additionalContentType;
            }
            if (suffix.equalsIgnoreCase("json")) {
                return Variant.APPLICATION_JSON;
            }
            String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
            if (contentType != null) {
                return contentType;
            }
        }

        return "application/octet-stream";
    }

    private static class ResourceTemplateLocation implements TemplateLocation {

        private final URL resource;
        private final Optional<Variant> variant;

        public ResourceTemplateLocation(URL resource, Variant variant) {
            this.resource = resource;
            this.variant = Optional.ofNullable(variant);
        }

        @Override
        public Reader read() {
            Charset charset = null;
            if (variant.isPresent()) {
                charset = variant.get().getCharset();
            }
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
            try {
                return new InputStreamReader(resource.openStream(), charset);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public Optional<Variant> getVariant() {
            return variant;
        }
    }

    private static class EngineProducerHolder {

        private static final EngineProducer INSTANCE = new EngineProducer();
    }

    public static EngineProducer getInstance() {
        return EngineProducerHolder.INSTANCE;
    }
}
