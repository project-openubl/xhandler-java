package io.github.carlosthe19916.webservices.utils;

import io.github.carlosthe19916.webservices.models.CdrResponseBean;
import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Iterator;

public class CdrUtils {

    private CdrUtils() {
        // just static methods
    }

    public static CdrResponseBean extractResponse(Document document) throws XPathExpressionException {
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

        return new CdrResponseBean(Integer.parseInt(code), description);
    }

    public static DocumentStatusResult getInfoFromCdrZip(byte[] zip) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        byte[] xml = Utils.getFirstXmlFileFromZip(zip);
        Document document = Utils.getDocumentFromBytes(xml);
        CdrResponseBean cdrContent = CdrUtils.extractResponse(document);

        Integer responseCode = cdrContent.getResponseCode();

        DocumentStatusResult.Status status;
        if (responseCode == 0) {
            status = DocumentStatusResult.Status.ACEPTADO;
        } else if (responseCode >= 100 && responseCode < 2_000) {
            status = DocumentStatusResult.Status.EXCEPCION;
        } else if (responseCode >= 2_000 && responseCode < 4_000) {
            status = DocumentStatusResult.Status.RECHAZADO;
        } else if (responseCode >= 4_000) {
            status = DocumentStatusResult.Status.ACEPTADO;
        } else {
            throw new IllegalStateException("CDR status code=" + responseCode + " not valid");
        }

        return new DocumentStatusResult(status, zip, cdrContent.getResponseCode(), cdrContent.getDescription());
    }

}
