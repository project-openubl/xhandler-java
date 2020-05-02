/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * <p>
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.eclipse.org/legal/epl-2.0/
 * <p>
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

    private SmartBillServiceManager() {
        // Just static methods
    }

    public static BillServiceModel send(File file, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
        return send(file.toPath(), username, password);
    }

    public static BillServiceModel send(Path path, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
        try {
            return send(Files.readAllBytes(path), username, password);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static BillServiceModel send(byte[] file, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
        XmlContentModel xmlContentModel;
        try {
            xmlContentModel = XmlContentProvider.getSunatDocument(new ByteArrayInputStream(file));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InvalidXMLFileException(e);
        }

        Optional<DocumentType> documentTypeOptional = DocumentType.valueFromDocumentType(xmlContentModel.getDocumentType());
        if (documentTypeOptional.isEmpty()) {
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

        try {
            switch (documentType) {
                case INVOICE:
                case CREDIT_NOTE:
                case DEBIT_NOTE:
                    return BillServiceManager.sendBill(fileNameWithoutExtension + ".xml", file, config);
                case VOIDED_DOCUMENT:
                case SUMMARY_DOCUMENT:
                    return BillServiceManager.sendSummary(fileNameWithoutExtension + ".xml", file, config);
                default:
                    throw new IllegalStateException("Not supported document");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
                    return config.getPerceptionAndRetentionDeliveryURL();
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
                    codigoDocumento = "01";
                } else if (Pattern.compile("^[B|b].*$").matcher(documentID).find()) {
                    codigoDocumento = "03";
                } else {
                    throw new IllegalStateException("Invalid Serie, can not detect code");
                }

                return MessageFormat.format("{0}-{1}-{2}", ruc, codigoDocumento, documentID);
            case CREDIT_NOTE:
                codigoDocumento = "07";
                return MessageFormat.format("{0}-{1}-{2}", ruc, codigoDocumento, documentID);
            case DEBIT_NOTE:
                codigoDocumento = "08";
                return MessageFormat.format("{0}-{1}-{2}", ruc, codigoDocumento, documentID);
            case VOIDED_DOCUMENT:
            case SUMMARY_DOCUMENT:
                return MessageFormat.format("{0}-{1}", ruc, documentID);
            default:
                throw new IllegalStateException("Invalid type of UBL Document, can not extract Serie-Numero to create fileName");
        }
    }
}
