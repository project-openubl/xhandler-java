package io.github.project.openubl.xbuilder.signature;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

public class XmlSignatureHelper {

    private XmlSignatureHelper() {
        // Helper class
    }

    public static DocumentBuilder newDocumentBuilder(Boolean disallowDoctypeDecl) throws ParserConfigurationException {
        return newDocumentBuilder(disallowDoctypeDecl, null);
    }

    public static DocumentBuilder newDocumentBuilder(Boolean disallowDoctypeDecl, Schema schema)
            throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        // avoid external entity attacks
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        boolean isDissalowDoctypeDecl = disallowDoctypeDecl == null ? true : disallowDoctypeDecl;
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", isDissalowDoctypeDecl);
        // avoid overflow attacks
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        if (schema != null) {
            dbf.setSchema(schema);
        }

        return dbf.newDocumentBuilder();
    }

    public static void transformNonTextNodeToOutputStream(
            Node node,
            OutputStream os,
            boolean omitXmlDeclaration,
            String encoding
    ) throws Exception { //NOPMD
        // previously we used javax.xml.transform.Transformer, however the JDK xalan implementation did not work correctly with a specified encoding
        // therefore we switched to DOMImplementationLS
        if (encoding == null) {
            encoding = "UTF-8";
        }
        DOMImplementationRegistry domImplementationRegistry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementationRegistry.getDOMImplementation(
                "LS"
        );
        LSOutput lsOutput = domImplementationLS.createLSOutput();
        lsOutput.setEncoding(encoding);
        lsOutput.setByteStream(os);
        LSSerializer lss = domImplementationLS.createLSSerializer();
        lss.getDomConfig().setParameter("xml-declaration", !omitXmlDeclaration);
        lss.write(node, lsOutput);
    }

    public static byte[] getBytesFromDocument(Document outputDoc) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        XmlSignatureHelper.transformNonTextNodeToOutputStream(outputDoc, outStream, false, "ISO-8859-1");
        return outStream.toByteArray();
    }

    public static Document convertStringToXMLDocument(String xmlString)
            throws ParserConfigurationException, IOException, SAXException {
        //API to obtain DOM Document instance
        DocumentBuilder builder = XmlSignatureHelper.newDocumentBuilder(true);
        //Parse the content to Document object
        return builder.parse(new InputSource(new StringReader(xmlString)));
    }
}
