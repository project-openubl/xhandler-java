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
package io.github.project.openubl.xmlsenderws.webservices.providers.errors;

import io.github.project.openubl.xmlsenderws.webservices.models.GetStatusResponseType;
import io.github.project.openubl.xmlsenderws.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.utils.Utils;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.BillConsultServiceWrapper;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billconsultservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
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
        String fileNameWithoutExtension = Utils.getFileNameWithoutExtension(fileName);
        Matcher matcher = Constants.FILENAME_STRUCTURE.matcher(fileNameWithoutExtension);
        if (matcher.matches()) {
            String[] split = fileNameWithoutExtension.split("-");

            String ruc = split[0];
            String tipo = split[1];
            String serie = split[2];
            int numero = Integer.parseInt(split[3]);

            ServiceConfig consultConfig = new ServiceConfig.Builder()
                    .url(Constants.DEFAULT_BILL_CONSULT_URL)
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .build();
            StatusResponse statusResponse = BillConsultServiceWrapper.getStatus(consultConfig, ruc, tipo, serie, numero);
            StatusResponse statusCdrResponse = BillConsultServiceWrapper.getStatusCdr(consultConfig, ruc, tipo, serie, numero);


            BillServiceModel result = new BillServiceModel();

            result.setCode(Integer.parseInt(statusResponse.getStatusCode()));
            result.setDescription(statusResponse.getStatusMessage());
            GetStatusResponseType.searchByCode(statusResponse.getStatusCode()).ifPresent(consultResponseType -> {
                switch (consultResponseType) {
                    case EXISTE_Y_ACEPTADO:
                        result.setStatus(BillServiceModel.Status.ACEPTADO);
                        break;
                    case EXISTE_PERO_RECHAZADO:
                        result.setStatus(BillServiceModel.Status.RECHAZADO);
                        break;
                    case EXISTE_PERO_DADO_DE_BAJA:
                        result.setStatus(BillServiceModel.Status.BAJA);
                        break;
                }
            });

            if (statusCdrResponse.getContent() != null) {
                try {
                    result.setCdr(statusCdrResponse.getContent());

                    BillServiceModel cdrStatusModel = Utils.toModel(statusCdrResponse.getContent());

                    result.setCode(cdrStatusModel.getCode());
                    result.setDescription(cdrStatusModel.getDescription());
                    if (result.getStatus() == null) {
                        result.setStatus(cdrStatusModel.getStatus());
                    }
                } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
                    throw new IllegalStateException(e);
                }
            }

            if (result.getStatus() == null && result.getCdr() == null) {
                result.setStatus(BillServiceModel.Status.RECHAZADO);
            }

            return result;
        } else {
            return null;
        }
    }

}
