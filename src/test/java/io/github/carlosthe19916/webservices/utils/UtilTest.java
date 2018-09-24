package io.github.carlosthe19916.webservices.utils;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UtilTest {

    /**
     * Should get the correct error code from javax.xml.ws.soap.SOAPFaultException
     * {@link io.github.carlosthe19916.webservices.utils.Util#getErrorCode(SOAPFaultException)}
     */
    @Test
    public void test_shouldExtractErrorCode() {
        // Fault code: 100
        SOAPFault mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("100");

        SOAPFaultException exception = new SOAPFaultException(mockFault);
        Optional<Integer> result = Util.getErrorCode(exception);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // Fault code: soap-env:Client.100
        mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("soap-env:Client.100");

        exception = new SOAPFaultException(mockFault);
        result = Util.getErrorCode(exception);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // exception message: 100
        SOAPFaultException mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("100");

        result = Util.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // exception message: soap-env:Client.100
        mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("soap-env:Client.100");

        result = Util.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());
    }

    /**
     * Should remove extension from fileName
     * {@link io.github.carlosthe19916.webservices.utils.Util#getFileNameWithoutExtension(String)}
     */
    @Test
    public void test_getFileNameWithoutExtension() {
        Assert.assertEquals("file", Util.getFileNameWithoutExtension("file"));
        Assert.assertEquals("file", Util.getFileNameWithoutExtension("file."));
        Assert.assertEquals("file", Util.getFileNameWithoutExtension("file.xml"));
        Assert.assertEquals("file.md", Util.getFileNameWithoutExtension("file.md.xml"));
    }

    /**
     * Should obtain the first XML file found on zip file
     * {@link io.github.carlosthe19916.webservices.utils.Util#getFirstXmlFileFromZip(byte[])}
     */
    @Test
    public void test_shouldReturnFirstXmlFound() throws URISyntaxException, IOException {
        final File cdrZipFile = Paths.get(getClass().getResource("/ubl/cdr/cdr.zip").toURI()).toFile();
        final byte[] cdrZipBytes = Files.readAllBytes(cdrZipFile.toPath());

        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/R-12345678901-01-F001-00000587.xml").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        final byte[] cdrExtractedBytes = Util.getFirstXmlFileFromZip(cdrZipBytes);

        Assert.assertTrue(Arrays.equals(cdrBytes, cdrExtractedBytes));
    }

    /**
     * Should parse byte[] to xml document
     * {@link io.github.carlosthe19916.webservices.utils.Util#getDocumentFromBytes(byte[])}
     */
    @Test
    public void getDocumentFromBytes() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        final File file = Paths.get(getClass().getResource("/ubl/F001-00005954.xml").toURI()).toFile();
        final byte[] bytes = Files.readAllBytes(file.toPath());

        Document document = Util.getDocumentFromBytes(bytes);
        Assert.assertNotNull(document);
    }
}