/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xsender.camel;

import io.github.project.openubl.xsender.Constants;
import io.github.project.openubl.xsender.camel.utils.CamelData;
import io.github.project.openubl.xsender.camel.utils.CamelUtils;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.BillServiceFileAnalyzer;
import io.github.project.openubl.xsender.files.BillServiceXMLFileAnalyzer;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.sunat.BillConsultServiceDestination;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import io.github.project.openubl.xsender.sunat.BillValidServiceDestination;
import jodd.io.ZipBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.main.Main;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static io.github.project.openubl.xsender.camel.utils.CamelUtils.getBillConsultService;
import static io.github.project.openubl.xsender.camel.utils.CamelUtils.getBillServiceCamelData;
import static io.github.project.openubl.xsender.camel.utils.CamelUtils.getBillValidService;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SendBillTest {

    CompanyURLs companyURLs = CompanyURLs
            .builder()
            .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
            .despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")
            .build();

    CompanyCredentials credentials = CompanyCredentials
            .builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .token("myToken")
            .build();

    private static CamelContext camelContext;

    @BeforeAll
    public static void beforeEach() {
        Main mainCamel = StandaloneCamel.getInstance().getMainCamel();
        if (!mainCamel.isStarted()) {
            mainCamel.start();
        }
        camelContext = mainCamel.getCamelContext();
    }

    @AfterAll
    public static void afterEach() {
        StandaloneCamel.getInstance().getMainCamel().stop();
    }

    @Test
    public void billService_sendBill_factura() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1.xml");
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNull(sunatResponse.getSunat().getTicket());
        assertNotNull(sunatResponse.getSunat().getCdr());
        assertNotNull(sunatResponse.getSunat().getCdr());
        assertEquals(Status.ACEPTADO, sunatResponse.getStatus());
        assertEquals(0, sunatResponse.getMetadata().getResponseCode());
        assertEquals("La Factura numero F001-1, ha sido aceptada", sunatResponse.getMetadata().getDescription());
        assertTrue(sunatResponse.getMetadata().getNotes().isEmpty());
    }

    @Test
    public void billService_sendBill_facturaGeneraRechazo() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1_alterado.xml");
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNull(sunatResponse.getSunat());
        assertEquals(Status.RECHAZADO, sunatResponse.getStatus());
        assertEquals(2335, sunatResponse.getMetadata().getResponseCode());
        assertEquals("El documento electrónico ingresado ha sido alterado - Detalle: Incorrect reference digest value", sunatResponse.getMetadata().getDescription());
        assertTrue(sunatResponse.getMetadata().getNotes().isEmpty());
    }

    @Test
    public void billService_sendBill_facturaGeneraException() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1_observado.xml");
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNull(sunatResponse.getSunat());
        assertEquals(Status.RECHAZADO, sunatResponse.getStatus());
        assertEquals(3244, sunatResponse.getMetadata().getResponseCode());
        assertThat(sunatResponse.getMetadata().getDescription(), CoreMatchers.containsString("Debe consignar la informacion del tipo de transaccion del comprobante"));
        assertTrue(sunatResponse.getMetadata().getNotes().isEmpty());
    }

    @Test
    public void billService_sendBill_retention() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/20494637074-20-R001-00000001.xml");
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertEquals(Status.ACEPTADO, sunatResponse.getStatus());
        assertEquals(0, sunatResponse.getMetadata().getResponseCode());
        assertEquals("El Comprobante numero R001-00000001 ha sido aceptado", sunatResponse.getMetadata().getDescription());
    }

    // TODO mock camel route and make this test work
    @Disabled
    @Test
    public void billService_sendBill_guiaRemision() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/20000000001-31-VVV1-1.xml");
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertEquals(Status.EXCEPCION, sunatResponse.getStatus());
        assertEquals(1085, sunatResponse.getMetadata().getResponseCode()); // Debe enviar las guias de remisión por el nuevo sistema de recepción de guias electronicas
    }

    @Test
    public void billService_getStatus() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-RA-20200328-1.xml");
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xmlFileIS, companyURLs);

        // First send file and generate ticket
        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination fileDestination = fileAnalyzer.getSendFileDestination();
        CamelData camelFileData = getBillServiceCamelData(zipFile, fileDestination, credentials);

        SunatResponse sendFileSunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelFileData.getBody(), camelFileData.getHeaders(), SunatResponse.class);

        assertNotNull(sendFileSunatResponse);
        assertNotNull(sendFileSunatResponse.getSunat().getTicket());

        // Verify ticket
        String ticket = sendFileSunatResponse.getSunat().getTicket();
        BillServiceDestination ticketDestination = fileAnalyzer.getVerifyTicketDestination();
        CamelData camelTicketData = CamelUtils.getBillServiceCamelData(ticket, ticketDestination, credentials);

        SunatResponse verifyTicketSunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelTicketData.getBody(), camelTicketData.getHeaders(), SunatResponse.class);

        assertNotNull(verifyTicketSunatResponse);
        assertNull(verifyTicketSunatResponse.getSunat().getTicket());
        assertNotNull(verifyTicketSunatResponse.getSunat().getCdr());
        assertNotNull(verifyTicketSunatResponse.getSunat().getCdr());
        assertEquals(Status.ACEPTADO, verifyTicketSunatResponse.getStatus());
        assertEquals(0, verifyTicketSunatResponse.getMetadata().getResponseCode());
        assertEquals("La Comunicacion de baja RA-20200328-1, ha sido aceptada", verifyTicketSunatResponse.getMetadata().getDescription());
        assertTrue(verifyTicketSunatResponse.getMetadata().getNotes().isEmpty());
    }

    @Test
    public void billService_sendSummary() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-RA-20200328-1.xml");
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNotNull(sunatResponse.getSunat().getTicket());
        assertNull(sunatResponse.getSunat().getCdr());
    }

    @Test
    public void billService_sendPack() throws Exception {
        byte[] xmlFileIS1 = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1.xml")
                .readAllBytes();
        byte[] xmlFileIS2 = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-RA-20200328-1.xml")
                .readAllBytes();

        byte[] zipFileBytes = ZipBuilder
                .createZipInMemory()
                .add(xmlFileIS1)
                .path("12345678912-01-F001-1.xml")
                .save()
                .add(xmlFileIS2)
                .path("12345678912-RA-20200328-1.xml")
                .save()
                .toBytes();

        ZipFile zipFile = ZipFile.builder()
                .file(zipFileBytes)
                .filename("12345678912-LT-20160405-1.zip")
                .build();
        BillServiceDestination destination = BillServiceDestination.builder()
                .url(companyURLs.getInvoice())
                .soapOperation(BillServiceDestination.SoapOperation.SEND_PACK)
                .build();
        CamelData camelData = getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertEquals(Status.EXCEPCION, sunatResponse.getStatus()); // It seems it only works in production, hence EXCEPTION
    }

    // Consult Service

    @Test
    public void consultService_getStatus() {
        BillConsultServiceDestination destination = BillConsultServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .operation(BillConsultServiceDestination.Operation.GET_STATUS)
                .build();

        CamelData camelData = getBillConsultService(
                "20494918910",
                "01",
                "F001",
                102,
                destination,
                credentials
        );

        CamelExecutionException exception = assertThrows(CamelExecutionException.class, () -> {
            service.sunat.gob.pe.billconsultservice.StatusResponse sunatResponse = camelContext
                    .createProducerTemplate()
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_CONSULT_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billconsultservice.StatusResponse.class);
        });

        assertEquals("El Usuario ingresado no existe", exception.getCause().getMessage());
    }

    @Test
    public void consultService_getStatusCdr() {
        BillConsultServiceDestination destination = BillConsultServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .operation(BillConsultServiceDestination.Operation.GET_STATUS_CDR)
                .build();

        CamelData camelData = getBillConsultService(
                "20494918910",
                "01",
                "F001",
                102,
                destination,
                credentials
        );

        CamelExecutionException exception = assertThrows(CamelExecutionException.class, () -> {
            service.sunat.gob.pe.billconsultservice.StatusResponse sunatResponse = camelContext
                    .createProducerTemplate()
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_CONSULT_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billconsultservice.StatusResponse.class);
        });

        assertEquals("El Usuario ingresado no existe", exception.getCause().getMessage());
    }

    // Consult valid service

    @Test
    public void consultValidService_validateData() {
        BillValidServiceDestination destination = BillValidServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .build();

        CamelData camelData = getBillValidService(
                "20494918910",
                "01",
                "F001",
                "102",
                "06",
                "12345678",
                "01-12-2022",
                120.5,
                "",
                destination,
                credentials
        );

        CamelExecutionException exception = assertThrows(CamelExecutionException.class, () -> {
            service.sunat.gob.pe.billvalidservice.StatusResponse sunatResponse = camelContext
                    .createProducerTemplate()
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_VALID_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billvalidservice.StatusResponse.class);
        });

        assertEquals("El Usuario ingresado no existe", exception.getCause().getMessage());
    }

    @Test
    public void consultValidService_validateFile() throws IOException {
        String fileName = "12345678912-01-F001-1.xml";
        byte[] fileContent = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/" + fileName)
                .readAllBytes();

        BillValidServiceDestination destination = BillValidServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .build();

        CamelData camelData = getBillValidService(
                fileName,
                fileContent,
                destination,
                credentials
        );

        CamelExecutionException exception = assertThrows(CamelExecutionException.class, () -> {
            service.sunat.gob.pe.billvalidservice.StatusResponse sunatResponse = camelContext
                    .createProducerTemplate()
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_VALID_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billvalidservice.StatusResponse.class);
        });

        assertEquals("El Usuario ingresado no existe", exception.getCause().getMessage());
    }
}
