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

import io.github.carlosthe19916.webservices.models.ConsultResponseType;
import io.github.carlosthe19916.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.utils.Utils;
import io.github.carlosthe19916.webservices.wrappers.BillConsultServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.BillValidServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billconsultservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Error1033BillServiceProvider extends AbstractErrorBillServiceProvider {

    private final static String DEFAULT_BILL_CONSULT_URL = "https://exception-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";
    private final static String DEFAULT_BILL_VALID_URL = "https://exception-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService";
    private final static Pattern FILENAME_STRUCTURE = Pattern.compile("(?:\\d{11}-)\\d{2}-[a-zA-Z_0-9]{4}-\\d{1,8}"); // [RUC]-[TIPO DOCUMENTO]-[SERIE]-[NUMERO]

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
        boolean isXmlValid = checkXmlFileValidity(fileName, file, config.getUsername(), config.getPassword());
        if (!isXmlValid) {
            return null;
        }

        // STEP: CHECK DOCUMENT VALIDITY
        StatusResponse statusResponse = checkDocumentStatus(fileName, config.getUsername(), config.getPassword());
        if (statusResponse == null) {
            return null;
        }

        // STEP: PROCESS
        try {
            Optional<ConsultResponseType> optional = ConsultResponseType.searchByCode(statusResponse.getStatusCode());
            if (optional.isPresent()) {
                switch (optional.get()) {
                    case EXISTE_Y_ACEPTADO: {
                        return Utils.toModel(statusResponse.getContent());
                    }
                    case EXISTE_PERO_RECHAZADO: {
                        return Utils.toModel(statusResponse.getContent());
                    }
                    case EXISTE_PERO_DADO_DE_BAJA: {
                        BillServiceModel model = Utils.toModel(statusResponse.getContent());
                        model.setStatus(BillServiceModel.Status.BAJA);
                        return model;
                    }
                    default: {
                        return null;
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | XPathExpressionException | SAXException e) {
            throw new IllegalStateException(e);
        }

        return null;
    }

    protected StatusResponse checkDocumentStatus(String fileName, String username, String password) {
        String fileNameWithoutExtension = Utils.getFileNameWithoutExtension(fileName);

        Matcher matcher = FILENAME_STRUCTURE.matcher(fileNameWithoutExtension);
        if (matcher.matches()) {

            ServiceConfig consultServiceConfig = new ServiceConfig.Builder()
                    .url(DEFAULT_BILL_CONSULT_URL)
                    .username(username)
                    .password(password)
                    .build();

            String[] split = fileNameWithoutExtension.split("-");
            return BillConsultServiceWrapper.getStatus(
                    consultServiceConfig,
                    split[0],
                    split[1],
                    split[2],
                    Integer.parseInt(split[3])
            );
        }
        return null;
    }

    protected boolean checkXmlFileValidity(String fileName, byte[] file, String username, String password) {
        try {
            ServiceConfig validServiceConfig = new ServiceConfig.Builder()
                    .url(DEFAULT_BILL_VALID_URL)
                    .username(username)
                    .password(password)
                    .build();

            byte[] xml = file;
            String xmlFileName = fileName;
            if (fileName.endsWith(".zip")) {
                xml = Utils.getFirstXmlFileFromZip(file);
                xmlFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".xml";
            }

            assert xml != null;
            byte[] encode = Base64.getEncoder().encode(xml);
            String xmlBase64Encoded = new String(encode);

            service.sunat.gob.pe.billvalidservice.StatusResponse statusResponse = BillValidServiceWrapper.getStatus(validServiceConfig, xmlFileName, xmlBase64Encoded);
            int statuscode = Integer.parseInt(statusResponse.getStatusCode());

            return statuscode == 0;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
