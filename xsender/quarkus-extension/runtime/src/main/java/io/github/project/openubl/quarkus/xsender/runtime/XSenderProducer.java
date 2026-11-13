package io.github.project.openubl.quarkus.xsender.runtime;

import io.github.project.openubl.xsender.XSender;
import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.sunat.SunatClient;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * Provides a shared {@link SunatClient} and an {@link XSender} for the company
 * configured in {@code quarkus.xsender.company.*}.
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
    XSender xsender(XSenderConfig config, SunatClient sunatClient) {
        XSenderConfig.Company company = config.company()
                .orElseThrow(() -> new XSenderException("Configure quarkus.xsender.company.* to inject an XSender"));
        CompanyURLs urls = new CompanyURLs(
                company.invoiceUrl().orElse(null),
                company.perceptionRetentionUrl().orElse(null),
                company.despatchUrl().orElse(null)
        );
        CompanyCredentials credentials = CompanyCredentials.builder()
                .username(company.username())
                .password(company.password())
                .clientId(company.clientId().orElse(null))
                .clientSecret(company.clientSecret().orElse(null))
                .build();
        return new XSender(urls, credentials, sunatClient);
    }
}
