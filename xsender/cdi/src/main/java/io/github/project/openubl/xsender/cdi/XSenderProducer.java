package io.github.project.openubl.xsender.cdi;

import io.github.project.openubl.xsender.XSender;
import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.sunat.SunatClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.Config;

import java.util.Optional;

/**
 * Provides a shared {@link SunatClient} and an {@link XSender} for the company
 * configured with MicroProfile Config:
 * <pre>
 * xsender.security-api-url=https://api-seguridad.sunat.gob.pe   (default)
 * xsender.company.invoice-url=...
 * xsender.company.perception-retention-url=...
 * xsender.company.despatch-url=...
 * xsender.company.username=...
 * xsender.company.password=...
 * xsender.company.client-id=...        (guías de remisión)
 * xsender.company.client-secret=...    (guías de remisión)
 * </pre>
 * Without {@code xsender.company.username} and {@code password}, injecting an
 * XSender fails with an {@link XSenderException} when the first bean using it is created.
 * <p>
 * To provide your own SunatClient or XSender (e.g. one per company), produce it
 * as an {@code @Alternative @Priority(1)} bean so it takes precedence over these.
 */
@ApplicationScoped
public class XSenderProducer {

    @Produces
    @Singleton
    public SunatClient sunatClient(Config config) {
        return new SunatClient(
                config.getOptionalValue("xsender.security-api-url", String.class).orElse(SunatClient.SUNAT_SECURITY_API_URL)
        );
    }

    @Produces
    @Singleton
    public XSender xsender(Config config, SunatClient sunatClient) {
        String username = optional(config, "username")
                .orElseThrow(() -> new XSenderException("Configure xsender.company.* to inject an XSender"));
        CompanyURLs urls = new CompanyURLs(
                optional(config, "invoice-url").orElse(null),
                optional(config, "perception-retention-url").orElse(null),
                optional(config, "despatch-url").orElse(null)
        );
        CompanyCredentials credentials = CompanyCredentials.builder()
                .username(username)
                .password(optional(config, "password")
                        .orElseThrow(() -> new XSenderException("xsender.company.password is required")))
                .clientId(optional(config, "client-id").orElse(null))
                .clientSecret(optional(config, "client-secret").orElse(null))
                .build();
        return new XSender(urls, credentials, sunatClient);
    }

    private static Optional<String> optional(Config config, String companyProperty) {
        return config.getOptionalValue("xsender.company." + companyProperty, String.class);
    }
}
