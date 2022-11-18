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
package io.github.project.openubl.xsender.discovery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.project.openubl.xsender.company.CompanyURLs;
import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class XMLFileAnalyzerTest {

    private final CompanyURLs urls = CompanyURLs
        .builder()
        .invoice("invoiceUrl")
        .perceptionRetention("perceptionRetentionUrl")
        .despatch("despatchUrl")
        .build();

    protected void assertZipFile(XMLFileAnalyzer xmlFileAnalyzer, String expectedZipFileName) {
        ZipFile zipFile = xmlFileAnalyzer.getZipFile();

        assertNotNull(zipFile);
        assertNotNull(zipFile.getFile());
        assertEquals(expectedZipFileName, zipFile.getFilename());
    }

    protected void assertFileDeliveryTarget(
        XMLFileAnalyzer xmlFileAnalyzer,
        String expectedUrl,
        FileDeliveryTarget.Method expectedMethod
    ) {
        FileDeliveryTarget fileDeliveryTarget = xmlFileAnalyzer.getFileDeliveryTarget();

        assertNotNull(fileDeliveryTarget);
        assertEquals(expectedUrl, fileDeliveryTarget.getUrl());
        assertEquals(expectedMethod, fileDeliveryTarget.getMethod());
    }

    protected void assertTicketEmpty(XMLFileAnalyzer xmlFileAnalyzer) {
        TicketDeliveryTarget ticketDeliveryTarget = xmlFileAnalyzer.getTicketDeliveryTarget();

        assertNull(ticketDeliveryTarget);
    }

    protected void assertTicketDeliveryTarget(XMLFileAnalyzer xmlFileAnalyzer, String expectedUrl) {
        TicketDeliveryTarget ticketDeliveryTarget = xmlFileAnalyzer.getTicketDeliveryTarget();

        assertNotNull(ticketDeliveryTarget);
        assertEquals(expectedUrl, ticketDeliveryTarget.getUrl());
    }

    @Test
    public void invoice_factura() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/invoice_factura.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-01-F001-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_BILL);
        assertTicketEmpty(xmlFileAnalyzer);
    }

    @Test
    public void invoice_boleta() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/invoice_boleta.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-03-B001-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_BILL);
        assertTicketEmpty(xmlFileAnalyzer);
    }

    //

    @Test
    public void invoiceWithZerosInDocumentID() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/invoice_with_zerosInID.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-01-F001-00000001.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_BILL);
        assertTicketEmpty(xmlFileAnalyzer);
    }

    //

    @Test
    public void creditNote_factura() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/credit-note_factura.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-07-FC01-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_BILL);
        assertTicketEmpty(xmlFileAnalyzer);
    }

    @Test
    public void creditNote_boleta() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/credit-note_boleta.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-07-BC01-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_BILL);
        assertTicketEmpty(xmlFileAnalyzer);
    }

    @Test
    public void debitNote_factura() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/debit-note_factura.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-08-FD01-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_BILL);
        assertTicketEmpty(xmlFileAnalyzer);
    }

    @Test
    public void debitNote_boleta() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/debit-note_boleta.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-08-BD01-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_BILL);
        assertTicketEmpty(xmlFileAnalyzer);
    }

    @Test
    public void voidedDocument_factura() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_factura.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-RA-20191224-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_SUMMARY);
        assertTicketDeliveryTarget(xmlFileAnalyzer, urls.getInvoice());
    }

    @Test
    public void voidedDocument_boleta() throws Exception {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_boleta.xml").toURI()).toFile();
        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(file, urls);

        assertZipFile(xmlFileAnalyzer, "12345678912-RA-20191224-1.zip");
        assertFileDeliveryTarget(xmlFileAnalyzer, urls.getInvoice(), FileDeliveryTarget.Method.SEND_SUMMARY);
        assertTicketDeliveryTarget(xmlFileAnalyzer, urls.getInvoice());
    }
}
