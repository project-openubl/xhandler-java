package io.github.project.openubl.xsender;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sends real documents to SUNAT's beta environment. It depends on that server
 * being up, so it only runs on demand: {@code mvn test -Dxsender.sunat-beta=true}
 */
@EnabledIfSystemProperty(named = "xsender.sunat-beta", matches = "true")
class SunatBetaTest {

    private final XSender xsender = new XSender(
            CompanyURLs.builder()
                    .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
                    .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
                    .build(),
            CompanyCredentials.builder()
                    .username("12345678959MODDATOS")
                    .password("MODDATOS")
                    .build()
    );

    @Test
    void invoice_isAccepted() throws IOException {
        SunatResponse response = xsender.send(xml("12345678912-01-F001-1.xml"));

        assertEquals(Status.ACEPTADO, response.getStatus());
        assertEquals(0, response.getMetadata().getResponseCode());
        assertNotNull(response.getSunat().getCdr());
    }

    @Test
    void alteredInvoice_isRejected() throws IOException {
        SunatResponse response = xsender.send(xml("12345678912-01-F001-1_alterado.xml"));

        assertEquals(Status.RECHAZADO, response.getStatus());
        assertEquals(2335, response.getMetadata().getResponseCode());
    }

    @Test
    void retention_isAccepted() throws IOException {
        SunatResponse response = xsender.send(xml("20494637074-20-R001-00000001.xml"));

        assertEquals(Status.ACEPTADO, response.getStatus());
    }

    @Test
    void voidedDocument_returnsATicket() throws IOException {
        SunatResponse response = xsender.send(xml("12345678912-RA-20200328-1.xml"));

        assertTrue(response.isTicket());
    }

    private static byte[] xml(String name) throws IOException {
        try (InputStream is = SunatBetaTest.class.getResourceAsStream("/ubl/" + name)) {
            return is.readAllBytes();
        }
    }
}
