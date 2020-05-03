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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class SmartBillServiceManagerTest {

    private String USERNAME = "12345678912MODDATOS";
    private String PASSWORD = "MODDATOS";

    @Before
    public void before() {
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

        Assert.assertTrue(exceptionWasThrew);
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

        Assert.assertTrue(exceptionWasThrew);
    }

    @Test
    public void sendInvoice_Factura() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/invoice_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("Invoice", xmlContentModel.getDocumentType());
        Assert.assertEquals("F001-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("La Factura numero F001-1, ha sido aceptada", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendInvoice_Boleta() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/invoice_boleta_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("Invoice", xmlContentModel.getDocumentType());
        Assert.assertEquals("B001-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("La Boleta numero B001-1, ha sido aceptada", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendCreditNote() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/credit-note_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("CreditNote", xmlContentModel.getDocumentType());
        Assert.assertEquals("F001-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("La Nota de Credito numero F001-1, ha sido aceptada", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendDebitNote() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/debit-note_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("DebitNote", xmlContentModel.getDocumentType());
        Assert.assertEquals("F001-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("La Nota de Debito numero F001-1, ha sido aceptada", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendVoidedDocument_ofFactura() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
        Assert.assertEquals("RA-20200328-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertEquals("01", xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNull(billServiceModel.getCdr());
        Assert.assertNull(billServiceModel.getCode());
        Assert.assertNull(billServiceModel.getDescription());
        Assert.assertNull(billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("La Comunicacion de baja RA-20200328-1, ha sido aceptada", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());
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

        Assert.assertTrue(exceptionWasThrew);
    }

    @Test
    public void sendVoidedDocument_ofPerception() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_perception_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
        Assert.assertEquals("RR-20200328-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertEquals("40", xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNull(billServiceModel.getCdr());
        Assert.assertNull(billServiceModel.getCode());
        Assert.assertNull(billServiceModel.getDescription());
        Assert.assertNull(billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("El Comprobante numero RR-20200328-1 ha sido aceptado", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());
    }

    @Test
    public void sendVoidedDocument_ofRetention() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_retention_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
        Assert.assertEquals("RR-20200328-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertEquals("20", xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNull(billServiceModel.getCdr());
        Assert.assertNull(billServiceModel.getCode());
        Assert.assertNull(billServiceModel.getDescription());
        Assert.assertNull(billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("El Comprobante numero RR-20200328-1 ha sido aceptado", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());
    }

    // No se puede dar de baja una guia de remision
//    @Test
//    public void sendVoidedDocument_ofDespachAdvice() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
//        File file = Paths.get(getClass().getResource("/xmls/voided-document_despatch-document_signed.xml").toURI()).toFile();
//
//        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);
//
//        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
//        Assert.assertEquals("VoidedDocuments", xmlContentModel.getDocumentType());
//        Assert.assertEquals("RR-20200328-1", xmlContentModel.getDocumentID());
//        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
//        Assert.assertEquals("09", xmlContentModel.getVoidedLineDocumentTypeCode());
//
//        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
//        Assert.assertNotNull(billServiceModel);
//        Assert.assertNull(billServiceModel.getCdr());
//        Assert.assertNull(billServiceModel.getCode());
//        Assert.assertNull(billServiceModel.getDescription());
//        Assert.assertNull(billServiceModel.getStatus());
//        Assert.assertNotNull(billServiceModel.getTicket());
//
//        // Check ticket
//        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
//        Assert.assertNotNull(billServiceModel);
//        Assert.assertNotNull(billServiceModel.getCdr());
//        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
//        Assert.assertEquals("El Comprobante numero RR-20200328-1 ha sido aceptado", billServiceModel.getDescription());
//        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
//        Assert.assertNotNull(billServiceModel.getTicket());
//    }

    @Test
    public void sendSummaryDocument() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/summary-document_signed.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("SummaryDocuments", xmlContentModel.getDocumentType());
        Assert.assertEquals("RC-20200328-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
        Assert.assertNull(billServiceModel.getCdr());
        Assert.assertNull(billServiceModel.getCode());
        Assert.assertNull(billServiceModel.getDescription());
        Assert.assertNull(billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());

        // Check ticket
        billServiceModel = SmartBillServiceManager.getStatus(billServiceModel.getTicket(), xmlContentModel, USERNAME, PASSWORD);
        Assert.assertNotNull(billServiceModel);
        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(0), billServiceModel.getCode());
        Assert.assertEquals("El Resumen diario RC-20200328-1, ha sido aceptado", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, billServiceModel.getStatus());
        Assert.assertNotNull(billServiceModel.getTicket());
    }

    @Test
    public void sendPerception() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/perception.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("Perception", xmlContentModel.getDocumentType());
        Assert.assertEquals("P001-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
//        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(2335), billServiceModel.getCode());
        Assert.assertEquals("My mock message", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.RECHAZADO, billServiceModel.getStatus());
        Assert.assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendInvalidRetention() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/retention.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("Retention", xmlContentModel.getDocumentType());
        Assert.assertEquals("R001-1", xmlContentModel.getDocumentID());
        Assert.assertEquals("12345678912", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
//        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(2335), billServiceModel.getCode());
        Assert.assertEquals("My mock message", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.RECHAZADO, billServiceModel.getStatus());
        Assert.assertNull(billServiceModel.getTicket());
    }

    @Test
    public void sendInvalidDespatchAdvice() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        File file = Paths.get(getClass().getResource("/xmls/despatch-advice.xml").toURI()).toFile();

        SmartBillServiceModel smartBillServiceModel = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        XmlContentModel xmlContentModel = smartBillServiceModel.getXmlContentModel();
        Assert.assertEquals("DespatchAdvice", xmlContentModel.getDocumentType());
        Assert.assertEquals("T001-123", xmlContentModel.getDocumentID());
        Assert.assertEquals("20123456789", xmlContentModel.getRuc());
        Assert.assertNull(xmlContentModel.getVoidedLineDocumentTypeCode());

        BillServiceModel billServiceModel = smartBillServiceModel.getBillServiceModel();
        Assert.assertNotNull(billServiceModel);
//        Assert.assertNotNull(billServiceModel.getCdr());
        Assert.assertEquals(Integer.valueOf(2335), billServiceModel.getCode());
        Assert.assertEquals("My mock message", billServiceModel.getDescription());
        Assert.assertEquals(BillServiceModel.Status.RECHAZADO, billServiceModel.getStatus());
        Assert.assertNull(billServiceModel.getTicket());
    }
}
