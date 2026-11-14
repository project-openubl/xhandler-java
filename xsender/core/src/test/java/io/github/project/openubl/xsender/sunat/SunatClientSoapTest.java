package io.github.project.openubl.xsender.sunat;

import io.github.project.openubl.xhandler.core.ErrorKind;
import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SunatClientSoapTest {

    private static final String PATH = "/ol-ti-itcpfegem-beta/billService";

    private final FakeSunat sunat = new FakeSunat();
    private final SunatClient client = new SunatClient(SunatClient.defaultHttpClient(), sunat.url(""));
    private final CompanyCredentials credentials = CompanyCredentials.builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS & <co>")
            .build();
    private final ZipFile zipFile = new ZipFile("zip-content".getBytes(StandardCharsets.UTF_8), "12345678959-01-F001-1.zip");

    @AfterEach
    void stopSunat() {
        sunat.close();
    }

    private BillServiceDestination destination(BillServiceDestination.SoapOperation operation) {
        return BillServiceDestination.builder().url(sunat.url(PATH)).soapOperation(operation).build();
    }

    @Test
    void sendBill_returnsTheCdr() {
        sunat.respondXml(PATH, 200, "/sunat/bill_service_response_ok.xml");

        SunatResponse response = client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials);

        assertEquals(Status.ACEPTADO, response.getStatus());
        assertEquals(0, response.getMetadata().getResponseCode());
        assertEquals("La Factura numero F001-1, ha sido aceptada", response.getMetadata().getDescription());
        assertNotNull(response.getSunat().getCdr());
        assertNull(response.getSunat().getTicket());
    }

    @Test
    void sendBill_postsTheSoapEnvelope() {
        sunat.respondXml(PATH, 200, "/sunat/bill_service_response_ok.xml");

        client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials);

        FakeSunat.Request request = sunat.lastRequest(PATH);
        assertEquals("POST", request.method());
        assertEquals("urn:sendBill", request.header("SOAPAction"));
        assertTrue(request.header("Content-Type").startsWith("text/xml"));
        assertTrue(request.body().contains("<wsse:Username>12345678959MODDATOS</wsse:Username>"));
        assertTrue(request.body().contains(">MODDATOS &amp; &lt;co&gt;</wsse:Password>"), "the password is escaped");
        assertTrue(request.body().contains("<ns2:sendBill xmlns:ns2=\"http://service.sunat.gob.pe\">"));
        assertTrue(request.body().contains("<fileName>12345678959-01-F001-1.zip</fileName>"));
        assertTrue(request.body().contains("<contentFile>" + Base64.getEncoder().encodeToString(zipFile.getFile()) + "</contentFile>"));
    }

    @Test
    void sendSummary_returnsTheTicket() {
        sunat.respondXml(PATH, 200, "/sunat/bill_service_response_ticket.xml");

        SunatResponse response = client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_SUMMARY), zipFile, credentials);

        assertTrue(response.isTicket());
        assertEquals("1703154974517", response.getSunat().getTicket());
        assertEquals("urn:sendSummary", sunat.lastRequest(PATH).header("SOAPAction"));
    }

    @Test
    void soapFault_isReturnedAsAResponse() {
        sunat.respondXml(PATH, 500, "/sunat/bill_service_response_fault.xml");

        SunatResponse response = client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials);

        assertEquals(Status.EXCEPCION, response.getStatus());
        assertEquals(111, response.getMetadata().getResponseCode());
        assertTrue(response.getMetadata().getDescription().startsWith("No tiene el perfil"));
    }

    @Test
    void soapFault_rejection() {
        sunat.respondXml(PATH, 500, "/sunat/bill_service_response_fault_rechazo.xml");

        SunatResponse response = client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials);

        assertEquals(Status.RECHAZADO, response.getStatus());
        assertEquals(2335, response.getMetadata().getResponseCode());
    }

    @Test
    void nonSoapResponse_throws() {
        XSenderException exception = assertThrows(XSenderException.class,
                () -> client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials));

        assertTrue(exception.getMessage().contains("HTTP 404"));
        assertEquals(1, sunat.requests(PATH).size(), "only proxy 401s are retried");
    }

    @Test
    void nonSoapResponse_isRetriedOnce() {
        sunat.respondSequence(PATH,
                new FakeSunat.Response(401, "text/html", "<html>401 Authorization Required</html>"),
                new FakeSunat.Response(200, "text/xml", FakeSunat.resource("/sunat/bill_service_response_ok.xml")));

        SunatResponse response = client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials);

        assertEquals(Status.ACEPTADO, response.getStatus());
        assertEquals(2, sunat.requests(PATH).size());
    }

    @Test
    void persistent401_throwsAfterTwoRetries() {
        sunat.respondHtml(PATH, 401, "<html>401 Authorization Required</html>");

        XSenderException error = assertThrows(XSenderException.class,
                () -> client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials));
        assertEquals(3, sunat.requests(PATH).size());
        assertEquals(ErrorKind.RATE_LIMITED, error.getKind());
    }

    @Test
    void xmlThatIsNotAnEnvelope_throws() {
        sunat.respondJson(PATH, 200, "<html><body>Mantenimiento</body></html>");

        assertThrows(XSenderException.class,
                () -> client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials));
    }

    @Test
    void envelopeWithoutCdrNorTicket_throws() {
        sunat.respondXml(PATH, 200, "/sunat/get_status_response_in_progress.xml");

        assertThrows(XSenderException.class,
                () -> client.sendFile(destination(BillServiceDestination.SoapOperation.SEND_BILL), zipFile, credentials));
    }

    @Test
    void unreachableSunat_throws() {
        BillServiceDestination destination = BillServiceDestination.builder()
                .url("http://localhost:1/billService")
                .soapOperation(BillServiceDestination.SoapOperation.SEND_BILL)
                .build();

        assertThrows(XSenderException.class, () -> client.sendFile(destination, zipFile, credentials));
    }

    @Test
    void getStatus_returnsTheCdr() {
        sunat.respondXml(PATH, 200, "/sunat/get_status_response_ok.xml");

        SunatResponse response = client.verifyTicket(destination(BillServiceDestination.SoapOperation.GET_STATUS), "123", credentials);

        assertEquals(Status.ACEPTADO, response.getStatus());
        assertEquals("La Comunicacion de baja RA-20200328-1, ha sido aceptada", response.getMetadata().getDescription());
        assertNotNull(response.getSunat().getCdr());
        assertEquals("urn:getStatus", sunat.lastRequest(PATH).header("SOAPAction"));
        assertTrue(sunat.lastRequest(PATH).body().contains("<ticket>123</ticket>"));
    }

    @Test
    void getStatus_inProgress() {
        sunat.respondXml(PATH, 200, "/sunat/get_status_response_in_progress.xml");

        SunatResponse response = client.verifyTicket(destination(BillServiceDestination.SoapOperation.GET_STATUS), "123", credentials);

        assertEquals(Status.EN_PROCESO, response.getStatus());
        assertEquals(98, response.getMetadata().getResponseCode());
    }

    @Test
    void getStatus_fault() {
        sunat.respondXml(PATH, 500, "/sunat/get_status_response_fault.xml");

        SunatResponse response = client.verifyTicket(destination(BillServiceDestination.SoapOperation.GET_STATUS), "123", credentials);

        assertEquals(127, response.getMetadata().getResponseCode());
        assertEquals("El ticket no existe", response.getMetadata().getDescription());
    }

    @Test
    void consultDocument_getStatusCdr() {
        sunat.respondXml("/consult", 200, "/sunat/get_status_cdr_response.xml");
        BillConsultServiceDestination destination = BillConsultServiceDestination.builder()
                .url(sunat.url("/consult"))
                .operation(BillConsultServiceDestination.Operation.GET_STATUS_CDR)
                .build();

        ConsultResponse response = client.consultDocument(destination, new DocumentQuery("12345678959", "01", "F001", 102), credentials);

        assertEquals("0004", response.statusCode());
        assertEquals("La constancia existe", response.statusMessage());
        assertArrayEquals(Base64.getDecoder().decode("UEsDBA=="), response.cdr());
        String body = sunat.lastRequest("/consult").body();
        assertEquals("urn:getStatusCdr", sunat.lastRequest("/consult").header("SOAPAction"));
        assertTrue(body.contains("<rucComprobante>12345678959</rucComprobante>"));
        assertTrue(body.contains("<numeroComprobante>102</numeroComprobante>"));
    }

    @Test
    void consultDocument_getStatus() {
        sunat.respondXml("/consult", 200, "/sunat/valida_cdp_response.xml");
        BillConsultServiceDestination destination = BillConsultServiceDestination.builder()
                .url(sunat.url("/consult"))
                .operation(BillConsultServiceDestination.Operation.GET_STATUS)
                .build();

        ConsultResponse response = client.consultDocument(destination, new DocumentQuery("12345678959", "01", "F001", 102), credentials);

        assertEquals("0", response.statusCode());
        assertEquals("urn:getStatus", sunat.lastRequest("/consult").header("SOAPAction"));
        assertTrue(sunat.lastRequest("/consult").body().contains("<ns2:getStatus xmlns:ns2=\"http://service.sunat.gob.pe\">"));
    }

    @Test
    void validateCdp() {
        sunat.respondXml("/valid", 200, "/sunat/valida_cdp_response.xml");
        BillValidServiceDestination destination = new BillValidServiceDestination(sunat.url("/valid"));
        CdpQuery query = new CdpQuery("12345678959", "01", "F001", "102", "6", "20123456789", "10/01/2024", new BigDecimal("118.00"), null);

        ConsultResponse response = client.validateCdp(destination, query, credentials);

        assertEquals("0", response.statusCode());
        assertEquals("El comprobante existe y está aceptado.", response.statusMessage());
        assertNull(response.cdr());
        String body = sunat.lastRequest("/valid").body();
        assertTrue(body.contains("<importeTotal>118.00</importeTotal>"));
        assertTrue(body.contains("<nroAutorizacion></nroAutorizacion>"));
    }

    @Test
    void consultService_faultIsReturnedAsAResponse() {
        sunat.respondXml("/valid", 500, "/sunat/bill_service_response_fault.xml");
        BillValidServiceDestination destination = new BillValidServiceDestination(sunat.url("/valid"));

        ConsultResponse response = client.validateFile(destination, "file.xml", new byte[]{1}, credentials);

        assertEquals("0111", response.statusCode());
        assertTrue(response.statusMessage().startsWith("No tiene el perfil"));
    }

    @Test
    void validateFile_postsTheFile() {
        sunat.respondXml("/valid", 200, "/sunat/valida_cdp_response.xml");
        BillValidServiceDestination destination = new BillValidServiceDestination(sunat.url("/valid"));

        client.validateFile(destination, "F001-1.xml", "abc".getBytes(StandardCharsets.UTF_8), credentials);

        FakeSunat.Request request = sunat.lastRequest("/valid");
        assertEquals("urn:verificaCPEarchivo", request.header("SOAPAction"));
        assertTrue(request.body().contains("<nombre>F001-1.xml</nombre>"));
        assertTrue(request.body().contains("<archivo>YWJj</archivo>"));
    }
}
