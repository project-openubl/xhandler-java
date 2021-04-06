package io.github.project.openubl.xsender;

import io.github.project.openubl.xmlsenderws.webservices.catalogs.Catalogo1;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.InvalidXMLFileException;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.UnsupportedDocumentTypeException;
import io.github.project.openubl.xmlsenderws.webservices.managers.BillServiceManager;
import io.github.project.openubl.xmlsenderws.webservices.models.DeliveryURLType;
import io.github.project.openubl.xmlsenderws.webservices.utils.UBLUtils;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import io.github.project.openubl.xmlsenderws.webservices.xml.DocumentType;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentModel;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentProvider;
import io.github.project.openubl.xsender.cxf.WsClientAuth;
import io.github.project.openubl.xsender.cxf.WsClientAuthBuilder;
import io.github.project.openubl.xsender.flyweight.WsClientFactory;
import io.github.project.openubl.xsender.manager.BillServiceAdapter;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billservice.BillService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class Sunat {

    private final WsClientFactory wsClientFactory;

    public Sunat(WsClientFactory wsClientFactory) {
        this.wsClientFactory = wsClientFactory;
    }

    public SunatResponse sendFile(CompanyConfig companyConfig, byte[] file) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
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
        String fileNameWithoutExtension = UBLUtils.getFileNameWithoutExtension(documentType, xmlContentModel.getRuc(), xmlContentModel.getDocumentID())
                .orElseThrow(() -> new IllegalStateException("Invalid type of UBL Document, can not extract Serie-Numero to create fileName"));

        SunatResponse response = new SunatResponse();

        WsClientAuth clientAuth = WsClientAuthBuilder.aWsClientAuth()
                .withAddress(deliveryURL)
                .withUsername(companyConfig.getUsername())
                .withPassword(companyConfig.getPassword())
                .build();
        BillService billService = wsClientFactory.getInstance(BillService.class, clientAuth);
        BillServiceAdapter billServiceAdapter = new BillServiceAdapter(billService);
        switch (documentType) {
            case INVOICE:
            case CREDIT_NOTE:
            case DEBIT_NOTE:
            case PERCEPTION:
            case RETENTION:
            case DESPATCH_ADVICE:
                byte[] cdr = billServiceAdapter.sendBill(fileNameWithoutExtension, file);
                response.setCdr(cdr);
                break;
            case VOIDED_DOCUMENT:
            case SUMMARY_DOCUMENT:
                String ticket = billServiceAdapter.sendSummary(fileNameWithoutExtension, file);
                response.setTicket(ticket);
                break;
            default:
                throw new IllegalStateException("Could not determine the correct service for the document");
        }

        return response;
    }

    public SunatResponse verifyTicket(CompanyConfig companyConfig, String ticket) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
        String deliveryURL = null;

        // Only for voided-document and not for summary-document
        Optional<Catalogo1> catalogo1 = Catalogo1.valueOfCode(xmlContentModel.getVoidedLineDocumentTypeCode());
        if (catalogo1.isPresent()) {
            switch (catalogo1.get()) {
                case PERCEPCION:
                case RETENCION:
                    deliveryURL = config.getPerceptionAndRetentionDeliveryURL();
                    break;
                // No se pueden dar bajas de guias de remision
//                case GUIA_REMISION_REMITENTE:
//                    deliveryURL = config.getDespatchAdviceDeliveryURL();
//                    break;
                default:
                    deliveryURL = config.getInvoiceAndNoteDeliveryURL();
            }
        }

        // If summary-document
        if (deliveryURL == null) {
            deliveryURL = config.getInvoiceAndNoteDeliveryURL();
        }

        ServiceConfig serviceConfig = new ServiceConfig.Builder()
                .url(deliveryURL)
                .username(username)
                .password(password)
                .build();

        return BillServiceManager.getStatus(ticket, serviceConfig);
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
