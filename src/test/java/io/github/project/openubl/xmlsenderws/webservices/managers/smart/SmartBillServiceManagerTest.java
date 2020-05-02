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
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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
                .withDespatchAdviceDeliveryURL("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService");
    }

    @Test
    public void sendInvoice() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException {
        File file = Paths.get(getClass().getResource("/xmls/invoice_signed.xml").toURI()).toFile();

        BillServiceModel result = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getCdr());
        Assert.assertEquals(Integer.valueOf(0), result.getCode());
        Assert.assertEquals("La Factura numero F001-1, ha sido aceptada", result.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
    }

    @Test
    public void sendCreditNote() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException {
        File file = Paths.get(getClass().getResource("/xmls/credit-note_signed.xml").toURI()).toFile();

        BillServiceModel result = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getCdr());
        Assert.assertEquals(Integer.valueOf(0), result.getCode());
        Assert.assertEquals("La Nota de Credito numero F001-1, ha sido aceptada", result.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
    }

    @Test
    public void sendDebitNote() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException {
        File file = Paths.get(getClass().getResource("/xmls/debit-note_signed.xml").toURI()).toFile();

        BillServiceModel result = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getCdr());
        Assert.assertEquals(Integer.valueOf(0), result.getCode());
        Assert.assertEquals("La Nota de Debito numero F001-1, ha sido aceptada", result.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
    }

    @Test
    public void sendVoidedDocument() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException {
        File file = Paths.get(getClass().getResource("/xmls/voided-document_signed.xml").toURI()).toFile();

        BillServiceModel result = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getTicket());
    }

    @Test
    public void sendSummaryDocument() throws URISyntaxException, InvalidXMLFileException, UnsupportedDocumentTypeException {
        File file = Paths.get(getClass().getResource("/xmls/summary-document_signed.xml").toURI()).toFile();

        BillServiceModel result = SmartBillServiceManager.send(file, USERNAME, PASSWORD);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getTicket());
    }

}
