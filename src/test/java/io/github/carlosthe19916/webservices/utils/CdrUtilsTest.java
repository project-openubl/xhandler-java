package io.github.carlosthe19916.webservices.utils;

import io.github.carlosthe19916.webservices.models.CdrResponseBean;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

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

}