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

import io.github.project.openubl.xsender.camel.utils.CamelData;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.FileAnalyzer;
import io.github.project.openubl.xsender.files.FileDestination;
import io.github.project.openubl.xsender.files.TicketDestination;
import io.github.project.openubl.xsender.files.XMLFileAnalyzer;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import jodd.io.ZipBuilder;
import org.apache.camel.CamelContext;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static io.github.project.openubl.xsender.camel.routes.CxfRouteBuilder.XSENDER_URI;
import static io.github.project.openubl.xsender.camel.utils.CamelUtils.getCamelData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CxfTest {

    CompanyURLs companyURLs = CompanyURLs
            .builder()
            .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .despatch("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
            .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
            .build();

    CompanyCredentials credentials = CompanyCredentials
            .builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .build();

    private static CamelContext camelContext;

    @BeforeAll
    public static void beforeEach() {
        camelContext = StandaloneCamel.getInstance().getMainCamel().getCamelContext();
    }

    @AfterAll
    public static void afterEach() {
        StandaloneCamel.getInstance().getMainCamel().stop();
    }

    @Test
    public void sendBill_factura() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

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
    public void sendBill_rechazado() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1_alterado.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNull(sunatResponse.getSunat());
        assertEquals(Status.RECHAZADO, sunatResponse.getStatus());
        assertEquals(2335, sunatResponse.getMetadata().getResponseCode());
        assertEquals("El documento electrónico ingresado ha sido alterado - Detalle: Incorrect reference digest value", sunatResponse.getMetadata().getDescription());
        assertTrue(sunatResponse.getMetadata().getNotes().isEmpty());
    }

    @Test
    public void sendBill_exception() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1_observado.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNull(sunatResponse.getSunat());
        assertEquals(Status.RECHAZADO, sunatResponse.getStatus());
        assertEquals(3244, sunatResponse.getMetadata().getResponseCode());
        assertThat(sunatResponse.getMetadata().getDescription(), CoreMatchers.containsString("Debe consignar la informacion del tipo de transaccion del comprobante"));
        assertTrue(sunatResponse.getMetadata().getNotes().isEmpty());
    }

    @Test
    public void sendBill_retention() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/20494637074-20-R001-00000001.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertEquals(Status.ACEPTADO, sunatResponse.getStatus());
        assertEquals(0, sunatResponse.getMetadata().getResponseCode());
        assertEquals("El Comprobante numero R001-00000001 ha sido aceptado", sunatResponse.getMetadata().getDescription());
    }

    @Test
    public void getStatus() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-RA-20200328-1.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        // First send file and generate ticket
        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination fileDestination = fileAnalyzer.getSendFileDestination();
        CamelData camelFileData = getCamelData(zipFile, fileDestination, credentials);

        SunatResponse sendFileSunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelFileData.getBody(), camelFileData.getHeaders(), SunatResponse.class);

        assertNotNull(sendFileSunatResponse);
        assertNotNull(sendFileSunatResponse.getSunat().getTicket());

        // Verify ticket
        String ticket = sendFileSunatResponse.getSunat().getTicket();
        TicketDestination ticketDestination = fileAnalyzer.getVerifyTicketDestination();
        CamelData camelTicketData = getCamelData(ticket, ticketDestination, credentials);

        SunatResponse verifyTicketSunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelTicketData.getBody(), camelTicketData.getHeaders(), SunatResponse.class);

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
    public void sendSummary() throws Exception {
        InputStream xmlFileIS = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-RA-20200328-1.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNotNull(sunatResponse.getSunat().getTicket());
        assertNull(sunatResponse.getSunat().getCdr());
    }

    @Disabled
    @Test
    public void sendPack() throws Exception {
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
        FileDestination destination = FileDestination.builder()
                .url(companyURLs.getInvoice())
                .operation(FileDestination.Operation.SEND_PACK)
                .build();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        assertNotNull(sunatResponse);
        assertNotNull(sunatResponse.getSunat().getTicket());
        assertNull(sunatResponse.getSunat().getCdr());
    }

}
