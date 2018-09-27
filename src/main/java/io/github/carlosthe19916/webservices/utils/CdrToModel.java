package io.github.carlosthe19916.webservices.utils;

import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.models.CdrResponseBean;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class CdrToModel {

    public CdrToModel() {
        // just static methods
    }

    public static BillServiceModel toModel(byte[] zip) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        byte[] xml = Utils.getFirstXmlFileFromZip(zip);
        Document document = Utils.getDocumentFromBytes(xml);
        CdrResponseBean cdrContent = CdrUtils.extractResponse(document);

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
