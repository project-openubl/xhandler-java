package io.github.carlosthe19916.webservices.utils;

import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.models.CdrModel;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    private Utils() {
        // Just static methods
    }

    public static Optional<Integer> getErrorCode(SOAPFaultException exception) {
        String errorCode = "";

        SOAPFault fault = exception.getFault();
        if (fault != null) {
            String faultCode = fault.getFaultCode();
            if (faultCode != null) {
                errorCode = faultCode.replaceAll("soap-env:Client.", "");
            }
        }

        if (!errorCode.matches("-?\\d+")) {
            String exceptionMessage = exception.getMessage();
            if (exceptionMessage != null) {
                errorCode = exceptionMessage.replaceAll("soap-env:Client.", "");
            }
        }

        if (!errorCode.matches("-?\\d+")) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(errorCode));
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

    public static byte[] getFirstXmlFileFromZip(byte[] data) throws IOException {
        try (
                ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data));
                ByteArrayOutputStream os = new ByteArrayOutputStream()
        ) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".xml")) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                    }
                    return os.toByteArray();
                }
            }

        }
        return null;
    }

    public static Document getDocumentFromBytes(byte[] cdrXml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(cdrXml));
    }

    public static CdrModel extractResponse(Document document) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if ("ar".equals(prefix)) {
                    return "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2";
                } else if ("ext".equals(prefix)) {
                    return "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2";
                } else if ("cbc".equals(prefix)) {
                    return "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
                } else if ("cac".equals(prefix)) {
                    return "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";
                }
                return null;
            }

            @Override
            public String getPrefix(String s) {
                return null;
            }

            @Override
            public Iterator getPrefixes(String s) {
                return null;
            }
        });

        XPathExpression codeXPathExpression = xPath.compile("//ar:ApplicationResponse/cac:DocumentResponse/cac:Response/cbc:ResponseCode");
        String code = (String) codeXPathExpression.evaluate(document, XPathConstants.STRING);

        XPathExpression descriptionXPathExpression = xPath.compile("//ar:ApplicationResponse/cac:DocumentResponse/cac:Response/cbc:Description");
        String description = (String) descriptionXPathExpression.evaluate(document, XPathConstants.STRING);

        return new CdrModel(Integer.parseInt(code), description);
    }

    public static BillServiceModel toModel(byte[] zip) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        byte[] xml = Utils.getFirstXmlFileFromZip(zip);
        Document document = Utils.getDocumentFromBytes(xml);
        CdrModel cdrContent = extractResponse(document);

        BillServiceModel result = new BillServiceModel();
        result.setCdr(zip);
        result.setCode(cdrContent.getResponseCode());
        result.setDescription(cdrContent.getDescription());
        result.setStatus(BillServiceModel.Status.fromCode(cdrContent.getResponseCode()));

        return result;
    }

    public static BillServiceModel toModel(String ticket) {
        BillServiceModel model = new BillServiceModel();
        model.setTicket(ticket);
        return model;
    }

}
