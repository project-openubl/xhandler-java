package io.github.project.openubl.spring.xsender.runtime;

import io.github.project.openubl.xsender.FileSender;
import io.github.project.openubl.xsender.sunat.SunatClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class XSenderAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(XSenderAutoConfiguration.class));

    @Test
    void providesASunatClient() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SunatClient.class);
            assertThat(context).doesNotHaveBean(FileSender.class);
        });
    }

    @Test
    void providesAnXSender_whenACompanyIsConfigured() {
        contextRunner
                .withPropertyValues(
                        "xsender.company.invoice-url=https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService",
                        "xsender.company.username=12345678959MODDATOS",
                        "xsender.company.password=MODDATOS"
                )
                .run(context -> assertThat(context).hasSingleBean(FileSender.class));
    }

    @Test
    void backsOff_whenTheApplicationDefinesItsOwnSunatClient() {
        SunatClient own = new SunatClient();
        contextRunner
                .withBean(SunatClient.class, () -> own)
                .run(context -> assertThat(context.getBean(SunatClient.class)).isSameAs(own));
    }

    @Test
    void isRegisteredAsAnAutoConfiguration() {
        assertThat(ImportCandidates.load(AutoConfiguration.class, getClass().getClassLoader()))
                .contains(XSenderAutoConfiguration.class.getName());
    }
}
