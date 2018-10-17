/*
 * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.carlosthe19916.webservices.providers.errors;

import io.github.carlosthe19916.webservices.catalogs.Catalogo1;
import io.github.carlosthe19916.webservices.models.ConsultResponseType;
import io.github.carlosthe19916.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.utils.Utils;
import io.github.carlosthe19916.webservices.wrappers.BillConsultServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billconsultservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;

public class Error1033BillServiceProvider extends AbstractErrorBillServiceProvider {

    private final Integer exceptionCode;
    private final BillServiceModel previousResult;

    public Error1033BillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public Error1033BillServiceProvider(Integer exceptionCode, BillServiceModel previousResult) {
        this.exceptionCode = exceptionCode;
        this.previousResult = previousResult;
    }

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {

        // STEP: CHECK XML FILE
//        boolean isXmlValid = checkXmlFileValidity(fileName, file, config.getUsername(), config.getPassword());
//        if (!isXmlValid) {
//            return null;
//        }

        // STEP: CHECK DOCUMENT VALIDITY
        StatusResponse statusResponse = checkDocumentStatus(fileName, config.getUsername(), config.getPassword());
        if (statusResponse == null) {
            return null;
        }

        // STEP: PROCESS
        BillServiceModel result = null;

        Optional<ConsultResponseType> consultResponseType = ConsultResponseType.searchByCode(statusResponse.getStatusCode());
        if (consultResponseType.isPresent()) {
            try {
                if (statusResponse.getContent() != null) {
                    result = Utils.toModel(statusResponse.getContent());
                    if (consultResponseType.get().equals(ConsultResponseType.EXISTE_PERO_DADO_DE_BAJA)) {
                        result.setStatus(BillServiceModel.Status.BAJA);
                    }
                } else {
                    result = new BillServiceModel();
                    result.setDescription(statusResponse.getStatusMessage());
                    switch (consultResponseType.get()) {
                        case EXISTE_Y_ACEPTADO:
                            result.setCode(0);
                            result.setStatus(BillServiceModel.Status.ACEPTADO);
                            break;
                        case EXISTE_PERO_RECHAZADO:
                            result.setCode(null);
                            result.setStatus(BillServiceModel.Status.RECHAZADO);
                            break;
                        case EXISTE_PERO_DADO_DE_BAJA:
                            result.setCode(0);
                            result.setStatus(BillServiceModel.Status.BAJA);
                            break;
                    }
                }
            } catch (IOException | ParserConfigurationException | XPathExpressionException | SAXException e) {
                throw new IllegalStateException(e);
            }
        }

        return result;
    }

    protected StatusResponse checkDocumentStatus(String fileName, String username, String password) {
        String fileNameWithoutExtension = Utils.getFileNameWithoutExtension(fileName);

        Matcher matcher = Constants.FILENAME_STRUCTURE.matcher(fileNameWithoutExtension);
        if (matcher.matches()) {

            ServiceConfig consultStatusServiceConfig = new ServiceConfig.Builder()
                    .url(Constants.DEFAULT_BILL_CONSULT_URL)
                    .username(username)
                    .password(password)
                    .build();

            String[] split = fileNameWithoutExtension.split("-");


            StatusResponse result = BillConsultServiceWrapper.getStatus(
                    consultStatusServiceConfig,
                    split[0],
                    split[1],
                    split[2],
                    Integer.parseInt(split[3])
            );

            ConsultResponseType.searchByCode(result.getStatusCode()).ifPresent(consultResponseType -> {
                if (
                        Catalogo1.FACTURA.getCode().equals(split[1]) &&
                                (
                                        consultResponseType.equals(ConsultResponseType.EXISTE_Y_ACEPTADO) ||
                                                consultResponseType.equals(ConsultResponseType.EXISTE_PERO_RECHAZADO) ||
                                                consultResponseType.equals(ConsultResponseType.EXISTE_PERO_DADO_DE_BAJA)
                                )
                ) {
                    StatusResponse statusCdr = BillConsultServiceWrapper.getStatusCdr(
                            consultStatusServiceConfig,
                            split[0],
                            split[1],
                            split[2],
                            Integer.parseInt(split[3]));
                    result.setContent(statusCdr.getContent());
                }
            });

            return result;
        }

        return null;
    }

//    protected boolean checkXmlFileValidity(String fileName, byte[] file, String username, String password) {
//        try {
//            ServiceConfig validServiceConfig = new ServiceConfig.Builder()
//                    .url(Constants.DEFAULT_BILL_VALID_URL)
//                    .username(username)
//                    .password(password)
//                    .build();
//
//            byte[] xml = file;
//            String xmlFileName = fileName;
//            if (fileName.endsWith(".zip")) {
//                xml = Utils.getFirstXmlFileFromZip(file);
//                xmlFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".xml";
//            }
//
//            assert xml != null;
//            byte[] encode = Base64.getEncoder().encode(xml);
//            String xmlBase64Encoded = new String(encode);
//
//            service.sunat.gob.pe.billvalidservice.StatusResponse statusResponse = BillValidServiceWrapper.getStatus(validServiceConfig, xmlFileName, xmlBase64Encoded);
//            int statuscode = Integer.parseInt(statusResponse.getStatusCode());
//
//            return statuscode == 0;
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
//    }
}
