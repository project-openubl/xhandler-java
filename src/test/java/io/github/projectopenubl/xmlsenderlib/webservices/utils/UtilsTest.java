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
package io.github.projectopenubl.xmlsenderlib.webservices.utils;

import io.github.projectopenubl.xmlsenderlib.webservices.models.CdrModel;
import io.github.projectopenubl.xmlsenderlib.webservices.providers.BillServiceModel;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UtilsTest {

    /**
     * Should get the correct error code from javax.xml.ws.soap.SOAPFaultException
     * {@link Utils#getErrorCode(SOAPFaultException)}
     */
    @Test
    public void test_shouldExtractErrorCode() {
        // Fault code: 100
        SOAPFault mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("100");

        SOAPFaultException exception = new SOAPFaultException(mockFault);
        Optional<Integer> result = Utils.getErrorCode(exception);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // Fault code: soap-env:Client.100
        mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("soap-env:Client.100");

        exception = new SOAPFaultException(mockFault);
        result = Utils.getErrorCode(exception);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // exception message: 100
        SOAPFaultException mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("100");

        result = Utils.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // exception message: soap-env:Client.100
        mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("soap-env:Client.100");

        result = Utils.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());
    }

    /**
     * Should remove extension from fileName
     * {@link Utils#getFileNameWithoutExtension(String)}
     */
    @Test
    public void test_getFileNameWithoutExtension() {
        Assert.assertEquals("file", Utils.getFileNameWithoutExtension("file"));
        Assert.assertEquals("file", Utils.getFileNameWithoutExtension("file."));
        Assert.assertEquals("file", Utils.getFileNameWithoutExtension("file.xml"));
        Assert.assertEquals("file.md", Utils.getFileNameWithoutExtension("file.md.xml"));
    }

    /**
     * Should obtain the first XML file found on zip file
     * {@link Utils#getFirstXmlFileFromZip(byte[])}
     */
    @Test
    public void test_shouldReturnFirstXmlFound() throws URISyntaxException, IOException {
        final File cdrZipFile = Paths.get(getClass().getResource("/ubl/cdr/cdr.zip").toURI()).toFile();
        final byte[] cdrZipBytes = Files.readAllBytes(cdrZipFile.toPath());

        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/R-12345678901-01-F001-00000587.xml").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        final byte[] cdrExtractedBytes = Utils.getFirstXmlFileFromZip(cdrZipBytes);

        Assert.assertTrue(Arrays.equals(cdrBytes, cdrExtractedBytes));
    }

    /**
     * Should parse byte[] to xml document
     * {@link Utils#getDocumentFromBytes(byte[])}
     */
    @Test
    public void getDocumentFromBytes() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        final File file = Paths.get(getClass().getResource("/ubl/F001-00005954.xml").toURI()).toFile();
        final byte[] bytes = Files.readAllBytes(file.toPath());

        Document document = Utils.getDocumentFromBytes(bytes);
        Assert.assertNotNull(document);
    }

    /**
     * Should extract info from document
     * {@link Utils#extractResponse(Document)}
     */
    @Test
    public void test_shouldExtractResponse() throws Exception {
        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/R-12345678901-01-F001-00000587.xml").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(cdrBytes));

        CdrModel response = Utils.extractResponse(document);

        Assert.assertNotNull(response);
        Assert.assertEquals(Integer.valueOf(0), response.getResponseCode());
        Assert.assertEquals("La Factura numero F001-00000587, ha sido aceptada", response.getDescription());
    }

    /**
     * Should create the correct model from zip
     * {@link Utils#toModel(byte[])}
     */
    @Test
    public void test_createModelFromZip() throws IOException, URISyntaxException, XPathExpressionException, SAXException, ParserConfigurationException {
        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/cdr.zip").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        BillServiceModel result = Utils.toModel(cdrBytes);

        Assert.assertNotNull(result.getCdr());
        Assert.assertEquals(Integer.valueOf(0), result.getCode());
        Assert.assertEquals("La Factura numero F001-00000587, ha sido aceptada", result.getDescription());
        Assert.assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
    }

}
