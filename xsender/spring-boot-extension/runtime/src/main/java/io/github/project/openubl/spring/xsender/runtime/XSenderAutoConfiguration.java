package io.github.project.openubl.spring.xsender.runtime;

import io.github.project.openubl.xsender.FileSender;
import io.github.project.openubl.xsender.sunat.SunatClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Provides a shared {@link SunatClient} and, when {@code xsender.company.*} is
 * configured, an {@link FileSender} for that company.
 */
@AutoConfiguration
@EnableConfigurationProperties(XSenderProperties.class)
public class XSenderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SunatClient sunatClient(XSenderProperties properties) {
        return new SunatClient(properties.securityApiUrl());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "xsender.company", name = "username")
    public FileSender xsender(XSenderProperties properties, SunatClient sunatClient) {
        XSenderProperties.Company company = properties.company();
        return new FileSender(company.urls(), company.credentials(), sunatClient);
    }
}
