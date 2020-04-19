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
package io.github.projectopenubl.xmlsenderlib.webservices.providers;

import io.github.projectopenubl.xmlsenderlib.webservices.exceptions.WebServiceExceptionFactory;
import io.github.projectopenubl.xmlsenderlib.webservices.models.TicketResponseType;
import io.github.projectopenubl.xmlsenderlib.webservices.utils.SunatErrors;
import io.github.projectopenubl.xmlsenderlib.webservices.utils.Utils;
import io.github.projectopenubl.xmlsenderlib.webservices.wrappers.BillServiceWrapper;
import io.github.projectopenubl.xmlsenderlib.webservices.wrappers.ServiceConfig;
import jodd.io.ZipBuilder;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultBillServiceProvider implements BillServiceProvider {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    private BillServiceModel sendSummaryOrPack(String fileName, byte[] file, ServiceConfig config, boolean isSummary) {
        if (fileName.endsWith(".xml")) {
            try {
                file = ZipBuilder.createZipInMemory()
                        .add(file)
                        .path(fileName)
                        .save()
                        .toBytes();
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        BillServiceModel result = null;
        try {
            String ticket;

            if (isSummary) {
                ticket = BillServiceWrapper.sendSummary(fileName, file, null, config);
            } else {
                ticket = BillServiceWrapper.sendPack(fileName, file, null, config);
            }

            result = Utils.toModel(ticket);
        } catch (SOAPFaultException e) {
            Set<ErrorBillServiceProviderFactory> factories = ErrorBillServiceRegistry.getInstance().getFactories(e);
            for (ErrorBillServiceProviderFactory factory : factories) {
                int exceptionCode = Utils.getErrorCode(e).orElseThrow(() -> new IllegalArgumentException("Could not get Sunat exception code"));

                ErrorBillServiceProvider provider = factory.create(exceptionCode);
                BillServiceModel handledResult = provider.sendSummary(fileName, file, config);
                if (handledResult != null) {
                    result = handledResult;
                }
            }

            if (result == null) {
                throw WebServiceExceptionFactory.createWebServiceException(e);
            }
        }

        return result;
    }

    private void sendSummaryOrPackCallback(ServiceConfig config, String ticket, BillServiceCallback callback, Map<String, Object> params, long delay) {
        Runnable runnable = () -> {
            try {
                BillServiceModel status = getStatus(ticket, config);

                switch (status.getStatus()) {
                    case ACEPTADO:
                        callback.onSuccess(params, status.getCode(), status.getDescription(), status.getCdr());
                        break;
                    case RECHAZADO:
                        callback.onError(params, status.getCode(), status.getDescription(), status.getCdr());
                        break;
                    case BAJA:
                        throw new IllegalStateException("Invalid status result=" + status.getStatus());
                    case EXCEPCION:
                        callback.onException(params, status.getCode(), status.getDescription());
                        break;
                    case EN_PROCESO:
                        callback.onProcess(params, status.getCode(), status.getDescription());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected status=" + status.getStatus());
                }
            } catch (SOAPFaultException e) {
                callback.onThrownException(params, e);
            }
        };

        executorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {
        if (fileName.endsWith(".xml")) {
            try {
                file = ZipBuilder.createZipInMemory()
                        .add(file)
                        .path(fileName)
                        .save()
                        .toBytes();
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        try {
            byte[] zip = BillServiceWrapper.sendBill(fileName, file, null, config);

            BillServiceModel result = Utils.toModel(zip);
            Integer statusCode = result.getCode();

            if (statusCode != null) {
                Set<ErrorBillServiceProviderFactory> factories = ErrorBillServiceRegistry.getInstance().getFactories(statusCode);
                for (ErrorBillServiceProviderFactory factory : factories) {
                    ErrorBillServiceProvider provider = factory.create(statusCode, result);

                    BillServiceModel handledResult = provider.sendBill(fileName, file, config);
                    if (handledResult != null) {
                        result = handledResult;
                    }
                }
            }
            return result;
        } catch (SOAPFaultException e) {
            Set<ErrorBillServiceProviderFactory> factories = ErrorBillServiceRegistry.getInstance().getFactories(e);
            for (ErrorBillServiceProviderFactory factory : factories) {
                int exceptionCode = Utils.getErrorCode(e).orElseThrow(() -> new IllegalArgumentException("Could not get Sunat exception code"));

                ErrorBillServiceProvider provider = factory.create(exceptionCode);
                BillServiceModel handledResult = provider.sendBill(fileName, file, config);
                if (handledResult != null) {
                    return handledResult;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config) {
        return sendSummary(fileName, file, config, Collections.emptyMap(), null, -1);
    }

    @Override
    public BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config, Map<String, Object> params, BillServiceCallback callback, long delay) {
        BillServiceModel result = sendSummaryOrPack(fileName, file, config, true);
        if (callback != null) {
            sendSummaryOrPackCallback(config, result.getTicket(), callback, params, delay);
        }
        return result;
    }

    @Override
    public BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config) {
        return sendPack(fileName, file, config, Collections.emptyMap(), null, -1);
    }

    @Override
    public BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config, Map<String, Object> params, BillServiceCallback callback, long delay) {
        BillServiceModel result = sendSummaryOrPack(fileName, file, config, false);
        if (callback != null) {
            sendSummaryOrPackCallback(config, result.getTicket(), callback, params, delay);
        }
        return result;
    }

    private static BillServiceModel getStatus(String ticket, ServiceConfig config, int statusCode, BillServiceModel previousModel) {
        Set<ErrorBillServiceProviderFactory> factories = ErrorBillServiceRegistry.getInstance().getFactories(statusCode);
        for (ErrorBillServiceProviderFactory factory : factories) {
            ErrorBillServiceProvider provider = factory.create(statusCode, previousModel);
            BillServiceModel result = provider.getStatus(ticket, config);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public BillServiceModel getStatus(String ticket, ServiceConfig config) {
        byte[] zip;
        int statusCode;

        try {
            StatusResponse statusResponse = BillServiceWrapper.getStatus(ticket, config);
            zip = statusResponse.getContent();
            statusCode = Integer.parseInt(statusResponse.getStatusCode());
        } catch (SOAPFaultException e) {
            Set<ErrorBillServiceProviderFactory> factories = ErrorBillServiceRegistry.getInstance().getFactories(e);
            for (ErrorBillServiceProviderFactory factory : factories) {
                int exceptionCode = Utils.getErrorCode(e).orElseThrow(() -> new IllegalArgumentException("Could not get Sunat exception code"));

                ErrorBillServiceProvider provider = factory.create(exceptionCode);
                BillServiceModel handledResult = provider.getStatus(ticket, config);
                if (handledResult != null) {
                    return handledResult;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        }

        try {


            BillServiceModel result;
            if (statusCode == TicketResponseType.PROCESO_CORRECTAMENTE.getCode()) {
                result = Utils.toModel(zip);
            } else if (statusCode == TicketResponseType.PROCESO_CON_ERRORES.getCode()) {
                BillServiceModel firstResult = Utils.toModel(zip);

                // Handle error 99
                result = getStatus(ticket, config, 99, firstResult);

                // If 99 is not handle then try to handle the error inside the xml
                if (result == null) {
                    result = getStatus(ticket, config, firstResult.getCode(), firstResult);
                }

                // If no result then FirstResult
                if (result == null) {
                    result = firstResult;
                }

                result.setTicket(ticket);
            } else if (statusCode == TicketResponseType.EN_PROCESO.getCode()) {
                result = new BillServiceModel();

                result.setCdr(zip);
                result.setTicket(ticket);
                result.setCode(statusCode);
                result.setStatus(BillServiceModel.Status.EN_PROCESO);
                result.setDescription(TicketResponseType.EN_PROCESO.getDescription());
            } else {
                result = getStatus(ticket, config, statusCode, null);

                if (result == null) {
                    String errorDescription = SunatErrors.getInstance().get(statusCode);
                    result = new BillServiceModel();

                    result.setCdr(zip);
                    result.setCode(statusCode);
                    result.setTicket(ticket);
                    result.setStatus(BillServiceModel.Status.fromCode(statusCode));
                    result.setDescription(errorDescription);
                }
            }

            result.setTicket(ticket);
            return result;
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

}
