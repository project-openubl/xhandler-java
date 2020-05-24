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
package io.github.project.openubl.xmlsenderws.webservices.managers.smart;

import io.github.project.openubl.xmlsenderws.webservices.catalogs.Catalogo1;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.InvalidXMLFileException;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.UnsupportedDocumentTypeException;
import io.github.project.openubl.xmlsenderws.webservices.managers.BillServiceManager;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import io.github.project.openubl.xmlsenderws.webservices.xml.DocumentType;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentModel;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentProvider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Pattern;

public class SmartBillServiceManager {

    private final static String FILENAME_FORMAT1 = "{0}-{1}-{2}";
    private final static String FILENAME_FORMAT2 = "{0}-{1}";

    private SmartBillServiceManager() {
        // Just static methods
    }

    public static SmartBillServiceModel send(File file, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        return send(file.toPath(), username, password);
    }

    public static SmartBillServiceModel send(Path path, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        return send(Files.readAllBytes(path), username, password);
    }

    public static SmartBillServiceModel send(byte[] file, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
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

        String deliveryURL = getDeliveryURL(documentType, xmlContentModel);
        String fileNameWithoutExtension = getFileNameWithoutExtension(documentType, xmlContentModel.getRuc(), xmlContentModel.getDocumentID());

        ServiceConfig config = new ServiceConfig.Builder()
                .url(deliveryURL)
                .username(username)
                .password(password)
                .build();

        BillServiceModel billServiceModel;
        switch (documentType) {
            case INVOICE:
            case CREDIT_NOTE:
            case DEBIT_NOTE:
            case PERCEPTION:
            case RETENTION:
            case DESPATCH_ADVICE:
                billServiceModel = BillServiceManager.sendBill(fileNameWithoutExtension + ".xml", file, config);
                break;
            case VOIDED_DOCUMENT:
            case SUMMARY_DOCUMENT:
                billServiceModel = BillServiceManager.sendSummary(fileNameWithoutExtension + ".xml", file, config);
                break;
            default:
                throw new IllegalStateException("Could not determine the correct service for the document");
        }

        return new SmartBillServiceModel(xmlContentModel, billServiceModel);
    }

    public static BillServiceModel getStatus(String ticket, XmlContentModel xmlContentModel, String username, String password) {
        SmartBillServiceConfig config = SmartBillServiceConfig.getInstance();

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

    private static String getDeliveryURL(DocumentType documentType, XmlContentModel xmlContentModel) {
        SmartBillServiceConfig config = SmartBillServiceConfig.getInstance();
        switch (documentType) {
            case INVOICE:
            case CREDIT_NOTE:
            case DEBIT_NOTE:
            case SUMMARY_DOCUMENT:
                return config.getInvoiceAndNoteDeliveryURL();
            case VOIDED_DOCUMENT:
                String tipoDocumentoAfectado = xmlContentModel.getVoidedLineDocumentTypeCode();
                Catalogo1 catalog1 = Catalogo1.valueOfCode(tipoDocumentoAfectado).orElseThrow(() -> new IllegalStateException("No se pudo convertir el valor del catálogo"));
                if (catalog1.equals(Catalogo1.PERCEPCION) || catalog1.equals(Catalogo1.RETENCION)) {
                    return config.getPerceptionAndRetentionDeliveryURL();
                } else if (catalog1.equals(Catalogo1.GUIA_REMISION_REMITENTE)) {
                    return config.getDespatchAdviceDeliveryURL();
                }
                return config.getInvoiceAndNoteDeliveryURL();
            case PERCEPTION:
            case RETENTION:
                return config.getPerceptionAndRetentionDeliveryURL();
            case DESPATCH_ADVICE:
                return config.getDespatchAdviceDeliveryURL();
            default:
                throw new IllegalStateException("Invalid type of UBL Document, can not create Sunat Server URL");
        }
    }

    private static String getFileNameWithoutExtension(DocumentType type, String ruc, String documentID) {
        String codigoDocumento;
        switch (type) {
            case INVOICE:
                if (Pattern.compile("^[F|f].*$").matcher(documentID).find()) {
                    codigoDocumento = Catalogo1.FACTURA.getCode();
                } else if (Pattern.compile("^[B|b].*$").matcher(documentID).find()) {
                    codigoDocumento = Catalogo1.BOLETA.getCode();
                } else {
                    throw new IllegalStateException("Invalid Serie, can not detect code");
                }

                return MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
            case CREDIT_NOTE:
                codigoDocumento = Catalogo1.NOTA_CREDITO.getCode();
                return MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
            case DEBIT_NOTE:
                codigoDocumento = Catalogo1.NOTA_DEBITO.getCode();
                return MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
            case VOIDED_DOCUMENT:
            case SUMMARY_DOCUMENT:
                return MessageFormat.format(FILENAME_FORMAT2, ruc, documentID);
            case PERCEPTION:
                codigoDocumento = Catalogo1.PERCEPCION.getCode();
                return MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
            case RETENTION:
                codigoDocumento = Catalogo1.RETENCION.getCode();
                return MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
            case DESPATCH_ADVICE:
                codigoDocumento = Catalogo1.GUIA_REMISION_REMITENTE.getCode();
                return MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
            default:
                throw new IllegalStateException("Invalid type of UBL Document, can not extract Serie-Numero to create fileName");
        }
    }
}
