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
package io.github.project.openubl.xmlsenderws.webservices.utils;

import io.github.project.openubl.xmlsenderws.webservices.catalogs.Catalogo1;
import io.github.project.openubl.xmlsenderws.webservices.models.DeliveryURLType;
import io.github.project.openubl.xmlsenderws.webservices.xml.DocumentType;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentModel;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Pattern;

public class UBLUtils {

    private final static String FILENAME_FORMAT1 = "{0}-{1}-{2}";
    private final static String FILENAME_FORMAT2 = "{0}-{1}";

    public UBLUtils() {
        // Only static methods
    }

    public static Optional<DeliveryURLType> getDeliveryURLType(DocumentType documentType, XmlContentModel xmlContentModel) {
        DeliveryURLType result = null;
        switch (documentType) {
            case INVOICE:
            case CREDIT_NOTE:
            case DEBIT_NOTE:
            case SUMMARY_DOCUMENT:
                result = DeliveryURLType.BASIC_DOCUMENTS_URL;
                break;
            case VOIDED_DOCUMENT:
                String tipoDocumentoAfectado = xmlContentModel.getVoidedLineDocumentTypeCode();
                Catalogo1 catalog1 = Catalogo1.valueOfCode(tipoDocumentoAfectado).orElseThrow(() -> new IllegalStateException("No se pudo convertir el valor del catálogo"));
                if (catalog1.equals(Catalogo1.PERCEPCION) || catalog1.equals(Catalogo1.RETENCION)) {
                    result = DeliveryURLType.PERCEPTION_RETENTION_URL;
                } else if (catalog1.equals(Catalogo1.GUIA_REMISION_REMITENTE)) {
                    result = DeliveryURLType.DESPATCH_DOCUMENT_URL;
                } else {
                    result = DeliveryURLType.BASIC_DOCUMENTS_URL;
                }
                break;
            case PERCEPTION:
            case RETENTION:
                result = DeliveryURLType.PERCEPTION_RETENTION_URL;
                break;
            case DESPATCH_ADVICE:
                result = DeliveryURLType.DESPATCH_DOCUMENT_URL;
                break;
        }

        return Optional.ofNullable(result);
    }

    public static Optional<String> getFileNameWithoutExtension(DocumentType type, String ruc, String documentID) {
        String result = null;

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

                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case CREDIT_NOTE:
                codigoDocumento = Catalogo1.NOTA_CREDITO.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case DEBIT_NOTE:
                codigoDocumento = Catalogo1.NOTA_DEBITO.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case VOIDED_DOCUMENT:
            case SUMMARY_DOCUMENT:
                result = MessageFormat.format(FILENAME_FORMAT2, ruc, documentID);
                break;
            case PERCEPTION:
                codigoDocumento = Catalogo1.PERCEPCION.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case RETENTION:
                codigoDocumento = Catalogo1.RETENCION.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case DESPATCH_ADVICE:
                codigoDocumento = Catalogo1.GUIA_REMISION_REMITENTE.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
        }

        return Optional.ofNullable(result);
    }

}
