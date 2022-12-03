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
package io.github.project.openubl.xsender.files.xml;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class XmlContentProviderTest {

    @Test
    public void getSunatDocument_invoice() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/invoice.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.INVOICE, model.getDocumentType());
        assertEquals("F001-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_invoice_withExtensionContent()
            throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ubl/12345678912-01-F001-1.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.INVOICE, model.getDocumentType());
        assertEquals("F001-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_creditNote() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/credit-note.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.CREDIT_NOTE, model.getDocumentType());
        assertEquals("BC01-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_debitNote() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/debit-note.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.DEBIT_NOTE, model.getDocumentType());
        assertEquals("BD01-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_voidedDocument() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/voided-document.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.VOIDED_DOCUMENT, model.getDocumentType());
        assertEquals("RA-20191224-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertEquals("01", model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_summaryDocument() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/summary-document.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.SUMMARY_DOCUMENT, model.getDocumentType());
        assertEquals("RC-20191224-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_perception() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/perception.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.PERCEPTION, model.getDocumentType());
        assertEquals("P001-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_retention() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/retention.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.RETENTION, model.getDocumentType());
        assertEquals("R001-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_despatchAdvice() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/despatch-advice.xml");
        assertNotNull(inputStream);

        // When
        XmlContent model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.DESPATCH_ADVICE, model.getDocumentType());
        assertEquals("T001-123", model.getDocumentID());
        assertEquals("20123456789", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }
}
