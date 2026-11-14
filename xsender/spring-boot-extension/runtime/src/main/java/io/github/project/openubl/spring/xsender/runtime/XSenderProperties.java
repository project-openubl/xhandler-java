package io.github.project.openubl.spring.xsender.runtime;

import io.github.project.openubl.xsender.Credentials;
import io.github.project.openubl.xsender.URLs;
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

        URLs urls() {
            return new URLs(invoiceUrl, perceptionRetentionUrl, despatchUrl);
        }

        Credentials credentials() {
            if (password == null) {
                throw new IllegalStateException("xsender.company.password is required");
            }
            return Credentials.builder()
                    .username(username)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
    }
}
