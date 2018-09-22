package io.github.carlosthe19916.webservices.utils;

import io.github.carlosthe19916.webservices.models.CdrResponseBean;
import org.w3c.dom.Document;

import javax.xml.xpath.*;

public class CdrUtils {

    private CdrUtils() {
        // just static methods
    }

    public static CdrResponseBean extractResponse(Document document) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        XPathExpression codeXPathExpression = xPath.compile("//ApplicationResponse/DocumentResponse/Response/ResponseCode");
        String code = (String) codeXPathExpression.evaluate(document, XPathConstants.STRING);

        XPathExpression descriptionXPathExpression = xPath.compile("//ApplicationResponse/DocumentResponse/Response/Description");
        String description = (String) descriptionXPathExpression.evaluate(document, XPathConstants.STRING);

        return new CdrResponseBean(code, description);
    }
}
