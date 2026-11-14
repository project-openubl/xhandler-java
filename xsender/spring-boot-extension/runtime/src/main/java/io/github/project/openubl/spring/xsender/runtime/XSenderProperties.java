package io.github.project.openubl.spring.xsender.runtime;

import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.sunat.SunatClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @param securityApiUrl where the guías de remisión access tokens are requested
 * @param company        the company an {@link io.github.project.openubl.xsender.FileSender} bean sends for, if any
 */
@ConfigurationProperties("xsender")
public record XSenderProperties(
        @DefaultValue(SunatClient.SUNAT_SECURITY_API_URL) String securityApiUrl,
        Company company
) {

    public record Company(
            String invoiceUrl,
            String perceptionRetentionUrl,
            String despatchUrl,
            String username,
            String password,
            String clientId,
            String clientSecret
    ) {
        @Override
        public String toString() {
            return "Company[username=" + username + ", invoiceUrl=" + invoiceUrl + "]";
        }

        CompanyURLs urls() {
            return new CompanyURLs(invoiceUrl, perceptionRetentionUrl, despatchUrl);
        }

        CompanyCredentials credentials() {
            if (password == null) {
                throw new XSenderException("xsender.company.password is required");
            }
            return CompanyCredentials.builder()
                    .username(username)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
    }
}
