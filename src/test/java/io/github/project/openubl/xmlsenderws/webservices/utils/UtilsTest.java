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
package io.github.project.openubl.xmlsenderws.webservices.utils;

import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.models.CdrModel;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertTrue(result.isPresent());
        assertEquals(100, (int) result.get());


        // Fault code: soap-env:Client.100
        mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("soap-env:Client.100");

        exception = new SOAPFaultException(mockFault);
        result = Utils.getErrorCode(exception);
        verify(mockFault).getFaultCode();

        assertTrue(result.isPresent());
        assertEquals(100, (int) result.get());


        // exception message: 100
        SOAPFaultException mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("100");

        result = Utils.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        assertTrue(result.isPresent());
        assertEquals(100, (int) result.get());


        // exception message: soap-env:Client.100
        mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("soap-env:Client.100");

        result = Utils.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        assertTrue(result.isPresent());
        assertEquals(100, (int) result.get());
    }

    /**
     * Should remove extension from fileName
     * {@link Utils#getFileNameWithoutExtension(String)}
     */
    @Test
    public void test_getFileNameWithoutExtension() {
        assertEquals("file", Utils.getFileNameWithoutExtension("file"));
        assertEquals("file", Utils.getFileNameWithoutExtension("file."));
        assertEquals("file", Utils.getFileNameWithoutExtension("file.xml"));
        assertEquals("file.md", Utils.getFileNameWithoutExtension("file.md.xml"));
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

        assertTrue(Arrays.equals(cdrBytes, cdrExtractedBytes));
    }

    /**
     * Should parse byte[] to xml document
     * {@link Utils#getDocumentFromBytes(byte[])}
     */
    @Test
    public void getDocumentFromBytes() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        final File file = Paths.get(getClass().getResource("/ubl/12345678912-01-F001-1.xml").toURI()).toFile();
        final byte[] bytes = Files.readAllBytes(file.toPath());

        Document document = Utils.getDocumentFromBytes(bytes);
        assertNotNull(document);
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

        assertNotNull(response);
        assertEquals(Integer.valueOf(0), response.getResponseCode());
        assertEquals("La Factura numero F001-00000587, ha sido aceptada", response.getDescription());
        assertEquals(0, response.getNotes().size());
    }

    /**
     * Should extract info from document
     * {@link Utils#extractResponse(Document)}
     */
    @Test
    public void test_shouldExtractResponse_withNotes() throws Exception {
        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/R-20220557805-01-F001-22Openubl.XML").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(cdrBytes));

        CdrModel response = Utils.extractResponse(document);

        assertNotNull(response);
        assertEquals(Integer.valueOf(0), response.getResponseCode());
        assertEquals("La Factura numero F001-22, ha sido aceptada", response.getDescription());

        assertEquals(8, response.getNotes().size());
        assertEquals("4252 - El dato ingresado como atributo @listName es incorrecto. - INFO: 4252 (nodo: \"cbc:InvoiceTypeCode/listName\" valor: \"SUNAT:Identificador de Tipo de Documento\")", response.getNotes().get(0));
        assertEquals("4252 - El dato ingresado como atributo @listName es incorrecto. - Error en la linea: 1: 4252 (nodo: \"cbc:PriceTypeCode/listName\" valor: \"SUNAT:Indicador de Tipo de Precio\")", response.getNotes().get(4));
        assertEquals("4252 - El dato ingresado como atributo @listName es incorrecto. - Error en la linea: 2 Tributo: 1000: 4252 (nodo: \"cbc:TaxExemptionReasonCode/listName\" valor: \"SUNAT:Codigo de Tipo de Afectacion del IGV\")", response.getNotes().get(7));
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

        assertNotNull(result.getCdr());
        assertEquals(Integer.valueOf(0), result.getCode());
        assertEquals("La Factura numero F001-00000587, ha sido aceptada", result.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
        assertEquals(0, result.getNotes().size());
    }

    /**
     * Should create the correct model from zip
     * {@link Utils#toModel(byte[])}
     */
    @Test
    public void test_createModelFromZip_withNotes() throws IOException, URISyntaxException, XPathExpressionException, SAXException, ParserConfigurationException {
        final File cdrFile = Paths.get(getClass().getResource("/ubl/cdr/cdr_openubl.zip").toURI()).toFile();
        final byte[] cdrBytes = Files.readAllBytes(cdrFile.toPath());

        BillServiceModel result = Utils.toModel(cdrBytes);

        assertNotNull(result.getCdr());
        assertEquals(Integer.valueOf(0), result.getCode());
        assertEquals("La Factura numero F001-22, ha sido aceptada", result.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());

        assertEquals(8, result.getNotes().size());
        assertEquals("4252 - El dato ingresado como atributo @listName es incorrecto. - INFO: 4252 (nodo: \"cbc:InvoiceTypeCode/listName\" valor: \"SUNAT:Identificador de Tipo de Documento\")", result.getNotes().get(0));
        assertEquals("4252 - El dato ingresado como atributo @listName es incorrecto. - Error en la linea: 1: 4252 (nodo: \"cbc:PriceTypeCode/listName\" valor: \"SUNAT:Indicador de Tipo de Precio\")", result.getNotes().get(4));
        assertEquals("4252 - El dato ingresado como atributo @listName es incorrecto. - Error en la linea: 2 Tributo: 1000: 4252 (nodo: \"cbc:TaxExemptionReasonCode/listName\" valor: \"SUNAT:Codigo de Tipo de Afectacion del IGV\")", result.getNotes().get(7));
    }

}
