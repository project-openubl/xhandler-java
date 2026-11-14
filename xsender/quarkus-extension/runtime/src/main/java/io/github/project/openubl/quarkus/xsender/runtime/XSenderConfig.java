package io.github.project.openubl.quarkus.xsender.runtime;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.sunat.SunatClient;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;

@ConfigMapping(prefix = "quarkus.xsender")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface XSenderConfig {

    /**
     * Where the guías de remisión access tokens are requested
     */
    @WithDefault(SunatClient.SUNAT_SECURITY_API_URL)
    String securityApiUrl();

    /**
     * The company an injected FileSender sends for
     */
    Optional<Company> company();

    interface Company {
        /**
         * SUNAT URL for invoices, credit/debit notes, summaries and voided documents
         */
        Optional<String> invoiceUrl();

        /**
         * SUNAT URL for perceptions and retentions
         */
        Optional<String> perceptionRetentionUrl();

        /**
         * SUNAT URL for guías de remisión
         */
        Optional<String> despatchUrl();

        /**
         * SOL user, e.g. 12345678959MODDATOS
         */
        String username();

        /**
         * SOL password
         */
        String password();

        /**
         * API client id, required for guías de remisión
         */
        Optional<String> clientId();

        /**
         * API client secret, required for guías de remisión
         */
        Optional<String> clientSecret();

        default CompanyURLs urls() {
            return new CompanyURLs(invoiceUrl().orElse(null), perceptionRetentionUrl().orElse(null), despatchUrl().orElse(null));
        }

        default CompanyCredentials credentials() {
            return CompanyCredentials.builder()
                    .username(username())
                    .password(password())
                    .clientId(clientId().orElse(null))
                    .clientSecret(clientSecret().orElse(null))
                    .build();
        }
    }
}
