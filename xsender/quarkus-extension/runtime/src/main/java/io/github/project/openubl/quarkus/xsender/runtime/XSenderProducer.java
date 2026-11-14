package io.github.project.openubl.quarkus.xsender.runtime;

import io.github.project.openubl.xsender.FileSender;
import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.sunat.SunatClient;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * Provides a shared {@link SunatClient} and an {@link FileSender} for the company
 * configured in {@code quarkus.xsender.company.*}. Without that configuration,
 * injecting an FileSender fails when the first bean using it is created.
 * <p>
 * Both are default beans: an application bean of the same type replaces them.
 */
@Singleton
public class XSenderProducer {

    @Produces
    @Singleton
    @DefaultBean
    SunatClient sunatClient(XSenderConfig config) {
        return new SunatClient(config.securityApiUrl());
    }

    @Produces
    @Singleton
    @DefaultBean
    FileSender xsender(XSenderConfig config, SunatClient sunatClient) {
        XSenderConfig.Company company = config.company()
                .orElseThrow(() -> new XSenderException("Configure quarkus.xsender.company.* to inject an FileSender"));
        return new FileSender(company.urls(), company.credentials(), sunatClient);
    }
}
