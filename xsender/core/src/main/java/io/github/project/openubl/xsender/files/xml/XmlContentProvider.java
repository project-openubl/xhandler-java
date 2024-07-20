package io.github.project.openubl.xsender.files.xml;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class XmlContentProvider {

    private XmlContentProvider() {
        // Only static methods
    }

    public static XmlContent getSunatDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        XmlHandler handler = new XmlHandler();

        SAXParserFactory factory = SAXParserFactory.newDefaultInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setNamespaceAware(true);

        SAXParser parser = factory.newSAXParser();
        parser.parse(is, handler);

        return handler.getModel();
    }
}
