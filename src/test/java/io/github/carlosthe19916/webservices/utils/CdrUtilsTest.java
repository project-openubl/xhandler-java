package io.github.carlosthe19916.webservices.utils;

import io.github.carlosthe19916.webservices.models.CdrResponseBean;
import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CdrUtilsTest {

    /**
     * Should extract info from document
     * {@link io.github.carlosthe19916.webservices.utils.CdrUtils#extractResponse(Document)}
     */
    @Test
    public void test_shouldExtractResponse() throws Exception {
        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/R-12345678901-01-F001-00000587.xml").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(cdrBytes));

        CdrResponseBean response = CdrUtils.extractResponse(document);

        Assert.assertNotNull(response);
        Assert.assertEquals(Integer.valueOf(0), response.getResponseCode());
        Assert.assertEquals("La Factura numero F001-00000587, ha sido aceptada", response.getDescription());
    }

    /**
     * Should extract info from CDR Zip
     * {@link io.github.carlosthe19916.webservices.utils.CdrUtils#getInfoFromCdrZip(byte[])}
     */
    @Test
    public void test_shoudlProcessCdrZip() throws IOException, URISyntaxException, XPathExpressionException, SAXException, ParserConfigurationException {
        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/cdr.zip").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        DocumentStatusResult result = CdrUtils.getInfoFromCdrZip(cdrBytes);

        Assert.assertNotNull(result.getCdr());
        Assert.assertEquals(Integer.valueOf(0), result.getCode());
        Assert.assertEquals("La Factura numero F001-00000587, ha sido aceptada", result.getDescription());
        Assert.assertEquals(DocumentStatusResult.Status.ACEPTADO, result.getStatus());
    }
}