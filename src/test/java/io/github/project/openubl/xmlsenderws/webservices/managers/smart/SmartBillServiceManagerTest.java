/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xmlsenderws.webservices.managers.smart;

import io.github.project.openubl.xmlsenderws.webservices.exceptions.InvalidXMLFileException;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.UnsupportedDocumentTypeException;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.ValidationWebServiceException;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class SmartBillServiceManagerTest {

    private String USERNAME = "12345678912MODDATOS";
    private String PASSWORD = "MODDATOS";

    @BeforeAll
    public static void before() {
        SmartBillServiceConfig.getInstance()
                .withInvoiceAndNoteDeliveryURL("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
                .withPerceptionAndRetentionDeliveryURL("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
                .withDespatchAdviceDeliveryURL("https://e-beta.sunat.gob.pe/ol-ti-itemision-guia-gem-beta/billService");
    }

    @Test
    public void sendInvalidFile_shouldThrow_InvalidXMLFileException() throws UnsupportedDocumentTypeException, IOException {
        boolean exceptionWasThrew = false;
        try {
            SmartBillServiceManager.send(new byte[]{0, 1, 2}, USERNAME, PASSWORD);
        } catch (InvalidXMLFileException e) {
            exceptionWasThrew = true;
        }

        assertTrue(exceptionWasThrew);
    }


    @Test
    public void sendInvalidFile_shouldThrow_UnsupportedDocumentTypeException() throws URISyntaxException, InvalidXMLFileException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/invalidUBLFile.xml").toURI()).toFile();

        boolean exceptionWasThrew = false;
        try {
            SmartBillServiceManager.send(file, USERNAME, PASSWORD);
        } catch (UnsupportedDocumentTypeException e) {
            exceptionWasThrew = true;
        }

        assertTrue(exceptionWasThrew);
    }

    @Test
    public void sendInvoice_Factura() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/invoice_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("Invoice", xmlContentModel.getDocumentType());
        assertEquals("F001-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("La Factura numero F001-1, ha sido aceptada", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendInvoice_Boleta() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/invoice_boleta_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("Invoice", xmlContentModel.getDocumentType());
        assertEquals("B001-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("La Boleta numero B001-1, ha sido aceptada", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendCreditNote() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/credit-note_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("CreditNote", xmlContentModel.getDocumentType());
        assertEquals("F001-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("La Nota de Credito numero F001-1, ha sido aceptada", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendDebitNote() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/debit-note_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("DebitNote", xmlContentModel.getDocumentType());
        assertEquals("F001-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("La Nota de Debito numero F001-1, ha sido aceptada", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendVoidedDocument_ofFactura() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
        assertEquals("RA-20200328-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertEquals("01", xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNull(billServiceModel.getCdr());
        assertNull(billServiceModel.getCode());
        assertNull(billServiceModel.getDescription());
        assertNull(billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("La Comunicacion de baja RA-20200328-1, ha sido aceptada", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());
    }

    @Test
    public void sendVoidedDocument_ofBoleta() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_boleta_signed.xml").toURI()).toFile();

        // Bajas de boletas no estan permitidas

        boolean exceptionWasThrew = false;
        try {
            SmartBillServiceManager.send(file, USERNAME, PASSWORD);
        } catch (ValidationWebServiceException e) {
            exceptionWasThrew = true;
        }

        assertTrue(exceptionWasThrew);
    }

    @Test
    public void sendVoidedDocument_ofPerception() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_perception_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
        assertEquals("RR-20200328-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertEquals("40", xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNull(billServiceModel.getCdr());
        assertNull(billServiceModel.getCode());
        assertNull(billServiceModel.getDescription());
        assertNull(billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("El Comprobante numero RR-20200328-1 ha sido aceptado", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());
    }

    @Test
    public void sendVoidedDocument_ofRetention() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_retention_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
        assertEquals("RR-20200328-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertEquals("20", xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNull(billServiceModel.getCdr());
        assertNull(billServiceModel.getCode());
        assertNull(billServiceModel.getDescription());
        assertNull(billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("El Comprobante numero RR-20200328-1 ha sido aceptado", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());
    }

    // No se puede dar de baja una guia de remision
//    @Test
//    public void sendVoidedDocument_ofDespachAdvice() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
//        File file = Paths.get(getClass().getResource("/xmls/voided-document_despatch-document_signed.xml").toURI()).toFile();
//
//        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);
//
//        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
//        assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
//        assertEquals("RR-20200328-1", xmlContentModel.getDocumentID());
//        assertEquals("12345678912", xmlContentModel.getRuc());
//        assertEquals("09", xmlContentModel.getVoidedLineDocumentTypeCode());
//
//        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
//        assertNotNull(billServiceModel);
//        assertNull(billServiceModel.getCdr());
//        assertNull(billServiceModel.getCode());
//        assertNull(billServiceModel.getDescription());
//        assertNull(billServiceModel.getStatus());
//        assertNotNull(billServiceModel.getTicket());
//
//        // Check ticket
//        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
//        assertNotNull(billServiceModel);
//        assertNotNull(billServiceModel.getCdr());
//        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
//        assertEquals("El Comprobante numero RR-20200328-1 ha sido aceptado", billServiceModel.getDescription());
//        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
//        assertNotNull(billServiceModel.getTicket());
//    }

    @Test
    public void sendSummaryDocument() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/summary-document_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("SummaryDocuments", xmlContentModel.getDocumentType());
        assertEquals("RC-20200328-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
        assertNull(billServiceModel.getCdr());
        assertNull(billServiceModel.getCode());
        assertNull(billServiceModel.getDescription());
        assertNull(billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        assertNotNull(billServiceModel);
        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        assertEquals("El Resumen diario RC-20200328-1, ha sido aceptado", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        assertNotNull(billServiceModel.getTicket());
    }

    @Test
    public void sendPerception() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/perception.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("Perception", xmlContentModel.getDocumentType());
        assertEquals("P001-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
//        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(2335), billServiceModel.getCode());
        assertEquals("My mock message", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.RECHAZADO, billServiceModel.getStatus());
        assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendInvalidRetention() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/retention.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("Retention", xmlContentModel.getDocumentType());
        assertEquals("R001-1", xmlContentModel.getDocumentID());
        assertEquals("12345678912", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
//        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(2335), billServiceModel.getCode());
        assertEquals("My mock message", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.RECHAZADO, billServiceModel.getStatus());
        assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendInvalidDespatchAdvice() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/despatch-advice.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        assertEquals("DespatchAdvice", xmlContentModel.getDocumentType());
        assertEquals("T001-123", xmlContentModel.getDocumentID());
        assertEquals("20123456789", xmlContentModel.getRuc());
        assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        assertNotNull(billServiceModel);
//        assertNotNull(billServiceModel.getCdr());
        assertEquals(Integer.valueOf(2335), billServiceModel.getCode());
        assertEquals("My mock message", billServiceModel.getDescription());
        assertEquals(BillServiceModel.Status.RECHAZADO, billServiceModel.getStatus());
        assertNull(billServiceModel.getTicket());
    }
}
