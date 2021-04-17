package io.github.project.openubl.xsender.response;

import io.github.project.openubl.xsender.utils.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Optional;

public class CdrReader {

    private final byte[] cdrZip;

    private final CdrModel cdrModel;
    private final Optional<CrdStatus> cdrStatus;

    public CdrReader(byte[] cdrZip) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        this.cdrZip = cdrZip;

        byte[] xml = Utils.getFirstXmlFileFromZip(cdrZip);
        Document document = Utils.getDocumentFromBytes(xml);
        this.cdrModel = DocumentToModel.toCdrModel(document);

        this.cdrStatus = CrdStatus.fromCode(this.cdrModel.getResponseCode());
    }

    public byte[] getCdrZip() {
        return cdrZip;
    }

    public CdrModel getCdrModel() {
        return cdrModel;
    }

    public Optional<CrdStatus> getCdrStatus() {
        return cdrStatus;
    }
}
