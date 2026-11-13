package io.github.project.openubl.xsender.cdi;

import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.sunat.SunatClient;
import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfigBuilder;
import org.eclipse.microprofile.config.Config;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XSenderProducerTest {

    private final XSenderProducer producer = new XSenderProducer();

    private static Config config(Map<String, String> properties) {
        return new SmallRyeConfigBuilder()
                .withSources(new PropertiesConfigSource(properties, "test", 100))
                .build();
    }

    @Test
    void producesAnXSenderForTheConfiguredCompany() {
        Config config = config(Map.of(
                "xsender.company.invoice-url", "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService",
                "xsender.company.username", "12345678959MODDATOS",
                "xsender.company.password", "MODDATOS"
        ));

        assertNotNull(producer.xsender(config, producer.sunatClient(config)));
    }

    @Test
    void producingAnXSenderWithoutCompany_throws() {
        Config config = config(Map.of());

        assertThrows(XSenderException.class, () -> producer.xsender(config, new SunatClient()));
    }
}
