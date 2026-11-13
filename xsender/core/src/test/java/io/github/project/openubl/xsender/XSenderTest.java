package io.github.project.openubl.xsender;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.sunat.FakeSunat;
import io.github.project.openubl.xsender.sunat.SunatClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XSenderTest {

    private final FakeSunat sunat = new FakeSunat();
    private final CompanyURLs urls = CompanyURLs.builder()
            .invoice(sunat.url("/invoice"))
            .perceptionRetention(sunat.url("/perception"))
            .despatch(sunat.url("/despatch"))
            .build();
    private final CompanyCredentials credentials = CompanyCredentials.builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .build();
    private final XSender xsender = new XSender(urls, credentials, new SunatClient(SunatClient.defaultHttpClient(), sunat.url("")));

    @AfterEach
    void stopSunat() {
        sunat.close();
    }

    @Test
    void sendsAnInvoiceWithSendBill() {
        sunat.respondXml("/invoice", 200, "/sunat/bill_service_response_ok.xml");

        SunatResponse response = xsender.send(xml("/xmls/invoice_factura.xml"));

        assertEquals(Status.ACEPTADO, response.getStatus());
        FakeSunat.Request request = sunat.lastRequest("/invoice");
        assertEquals("urn:sendBill", request.header("SOAPAction"));
        assertTrue(request.body().contains("<fileName>12345678912-01-F001-1.zip</fileName>"));
    }

    @Test
    void sendsAPerceptionToThePerceptionUrl() {
        sunat.respondXml("/perception", 200, "/sunat/bill_service_response_ok.xml");

        xsender.send(xml("/xmls/perception.xml"));

        assertEquals("urn:sendBill", sunat.lastRequest("/perception").header("SOAPAction"));
    }

    @Test
    void sendsAVoidedDocumentAndVerifiesItsTicket() {
        sunat.respondXml("/invoice", 200, "/sunat/bill_service_response_ticket.xml");
        Path voided = xml("/xmls/voided-document.xml");

        SunatResponse sent = xsender.send(voided);
        assertEquals("urn:sendSummary", sunat.lastRequest("/invoice").header("SOAPAction"));

        sunat.respondXml("/invoice", 200, "/sunat/get_status_response_ok.xml");
        SunatResponse verified = xsender.verifyTicket(voided, sent.getSunat().getTicket());

        assertEquals(Status.ACEPTADO, verified.getStatus());
        assertEquals("urn:getStatus", sunat.lastRequest("/invoice").header("SOAPAction"));
        assertTrue(sunat.lastRequest("/invoice").body().contains("<ticket>1703154974517</ticket>"));
    }

    @Test
    void verifyTicket_ofAnInvoice_throws() {
        assertThrows(XSenderException.class, () -> xsender.verifyTicket(xml("/xmls/invoice_factura.xml"), "123"));
    }

    @Test
    void send_ofAnUnsupportedXml_throws() {
        assertThrows(XSenderException.class, () -> xsender.send("<foo/>".getBytes()));
    }

    private static Path xml(String resource) {
        try {
            return Path.of(XSenderTest.class.getResource(resource).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
