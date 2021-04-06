package io.github.project.openubl.xsender;

import io.github.project.openubl.xmlsenderws.webservices.exceptions.InvalidXMLFileException;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.UnsupportedDocumentTypeException;
import io.github.project.openubl.xmlsenderws.webservices.models.DeliveryURLType;
import io.github.project.openubl.xmlsenderws.webservices.utils.UBLUtils;
import io.github.project.openubl.xmlsenderws.webservices.xml.DocumentType;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentModel;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentProvider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class SunatAddressDiscovery {

    public void a(byte[] file) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
        XmlContentModel xmlContentModel;
        try {
            xmlContentModel = XmlContentProvider.getSunatDocument(new ByteArrayInputStream(file));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InvalidXMLFileException(e);
        }

        Optional<DocumentType> documentTypeOptional = DocumentType.valueFromDocumentType(xmlContentModel.getDocumentType());
        if (!documentTypeOptional.isPresent()) {
            throw new UnsupportedDocumentTypeException(xmlContentModel.getDocumentType() + " is not supported yet");
        }

        DocumentType documentType = documentTypeOptional.get();

        String deliveryURL = getDeliveryURL(companyConfig, documentType, xmlContentModel);


    }

    public static String getDeliveryURL(CompanyConfig addresses, DocumentType documentType, XmlContentModel xmlContentModel) {
        DeliveryURLType deliveryURLType = UBLUtils.getDeliveryURLType(documentType, xmlContentModel)
                .orElseThrow(() -> new IllegalStateException("Invalid type of UBL Document, can not create Sunat Server URL"));

        switch (deliveryURLType) {
            case BASIC_DOCUMENTS_URL:
                return addresses.getInvoiceUrl();
            case DESPATCH_DOCUMENT_URL:
                return addresses.getDespatchUrl();
            case PERCEPTION_RETENTION_URL:
                return addresses.getPerceptionUrl();
            default:
                throw new IllegalStateException("DeliveryURLType not configured to return a value");
        }
    }

}
