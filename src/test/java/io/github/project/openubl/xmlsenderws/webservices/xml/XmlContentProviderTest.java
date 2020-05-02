package io.github.project.openubl.xmlsenderws.webservices.xml;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XmlContentProviderTest {

    @Test
    public void getSunatDocument_invoice() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/invoice.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.INVOICE.getDocumentType(), model.getDocumentType());
        assertEquals("F001-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_invoice_withExtensionContent() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("ubl/20494637074-01-F001-00000001.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.INVOICE.getDocumentType(), model.getDocumentType());
        assertEquals("F001-00000001", model.getDocumentID());
        assertEquals("20494637074", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_creditNote() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/credit-note.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.CREDIT_NOTE.getDocumentType(), model.getDocumentType());
        assertEquals("BC01-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_debitNote() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/debit-note.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.DEBIT_NOTE.getDocumentType(), model.getDocumentType());
        assertEquals("BD01-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_voidedDocument() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/voided-document.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.VOIDED_DOCUMENT.getDocumentType(), model.getDocumentType());
        assertEquals("RA-20191224-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertEquals("01", model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_summaryDocument() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/summary-document.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.SUMMARY_DOCUMENT.getDocumentType(), model.getDocumentType());
        assertEquals("RC-20191224-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_perception() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/perception.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.PERCEPTION.getDocumentType(), model.getDocumentType());
        assertEquals("P001-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_retention() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/retention.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.RETENTION.getDocumentType(), model.getDocumentType());
        assertEquals("R001-1", model.getDocumentID());
        assertEquals("12345678912", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }

    @Test
    public void getSunatDocument_despatchAdvice() throws IOException, SAXException, ParserConfigurationException {
        // Given
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xmls/despatch-advice.xml");
        assertNotNull(inputStream);

        // When
        XmlContentModel model = XmlContentProvider.getSunatDocument(inputStream);

        // Then
        assertNotNull(model);
        assertEquals(DocumentType.DESPATCH_ADVICE.getDocumentType(), model.getDocumentType());
        assertEquals("T001-123", model.getDocumentID());
        assertEquals("20123456789", model.getRuc());
        assertNull(model.getVoidedLineDocumentTypeCode());
    }
}
