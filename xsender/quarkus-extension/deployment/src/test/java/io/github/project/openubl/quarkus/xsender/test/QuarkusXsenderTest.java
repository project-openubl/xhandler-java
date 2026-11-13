package io.github.project.openubl.quarkus.xsender.test;

import io.github.project.openubl.xsender.XSender;
import io.github.project.openubl.xsender.sunat.SunatClient;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuarkusXsenderTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class))
            .overrideConfigKey("quarkus.xsender.company.invoice-url", "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .overrideConfigKey("quarkus.xsender.company.username", "12345678959MODDATOS")
            .overrideConfigKey("quarkus.xsender.company.password", "MODDATOS");

    @Inject
    SunatClient sunatClient;

    @Inject
    XSender xsender;

    @Test
    public void injectsTheSunatClientAndTheConfiguredXSender() {
        assertNotNull(sunatClient);
        assertNotNull(xsender);
    }
}
