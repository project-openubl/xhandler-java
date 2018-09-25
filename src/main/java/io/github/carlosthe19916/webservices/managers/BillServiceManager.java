package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.exceptions.WebServiceExceptionFactory;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandlerFactory;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandlerFactoryManager;
import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.models.SendSummaryResult;
import io.github.carlosthe19916.webservices.models.types.ConsultaTicketResponseType;
import io.github.carlosthe19916.webservices.utils.CdrUtils;
import io.github.carlosthe19916.webservices.wrappers.BillServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import jodd.io.ZipBuilder;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class BillServiceManager {

    private BillServiceManager() {
        // Just static methods
    }

    /**
     * @param file   archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static DocumentStatusResult sendBill(File file, ServiceConfig config) throws IOException {
        return sendBill(file.toPath(), config);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static DocumentStatusResult sendBill(Path path, ServiceConfig config) throws IOException {
        return sendBill(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static DocumentStatusResult sendBill(String fileName, byte[] file, ServiceConfig config) throws IOException {
        if (fileName.endsWith(".xml")) {
            file = ZipBuilder.createZipInMemory()
                    .add(file)
                    .path(fileName)
                    .save()
                    .toBytes();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        }

        try {
            byte[] zip = BillServiceWrapper.sendBill(fileName, file, null, config);
            return CdrUtils.processZip(zip);
        } catch (SOAPFaultException e) {
            Set<BillServiceErrorHandlerFactory> factories = BillServiceErrorHandlerFactoryManager
                    .getInstance()
                    .getApplicableErrorHandlers(e);
            for (BillServiceErrorHandlerFactory factory : factories) {
                BillServiceErrorHandler errorHandler = factory.create(e);
                DocumentStatusResult result = errorHandler.sendBill(fileName, file, null, config);
                if (result != null) {
                    return result;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        } catch (SAXException | ParserConfigurationException | XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param ticket numero de ticket a ser consultado
     * @param config Credenciales y URL de destino de la petición
     */
    public static DocumentStatusResult getStatus(String ticket, ServiceConfig config) {
        try {
            StatusResponse statusResponse = BillServiceWrapper.getStatus(ticket, config);
            int statusCode = Integer.parseInt(statusResponse.getStatusCode());


            DocumentStatusResult statusResult;
            if (statusCode == ConsultaTicketResponseType.PROCESO_CORRECTAMENTE.getCode()) {
                statusResult = CdrUtils.processZip(statusResponse.getContent());
            } else if (statusCode == ConsultaTicketResponseType.PROCESO_CON_ERRORES.getCode()) {
                // Handle error 99
                statusResult = getStatusHandleErrors(ticket, config, 99);

                // If 99 is not handle then try to handle the error inside the xml
                if (statusResult == null) {
                    statusResult = CdrUtils.processZip(statusResponse.getContent());
                    Integer errorCode = statusResult.getCode();

                    statusResult = getStatusHandleErrors(ticket, config, errorCode);
                }

                // If no result then build it manually
                if (statusResult == null) {
                    statusResult = new DocumentStatusResult(
                            DocumentStatusResult.Status.RECHAZADO,
                            statusResponse.getContent(),
                            statusCode,
                            ConsultaTicketResponseType.PROCESO_CON_ERRORES.getDescription()
                    );
                }
            } else { // OTHERS
                statusResult = getStatusHandleErrors(ticket, config, statusCode);

                if (statusResult == null) {
                    String errorDescription = SUNATCodigoErrores.getInstance().get(statusCode);
                    statusResult = new DocumentStatusResult(
                            DocumentStatusResult.Status.EXCEPCION,
                            statusResponse.getContent(),
                            statusCode,
                            errorDescription
                    );
                }
            }
            return statusResult;
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static DocumentStatusResult getStatusHandleErrors(String ticket, ServiceConfig config, int statusCode) {
        Set<BillServiceErrorHandlerFactory> factories = BillServiceErrorHandlerFactoryManager
                .getInstance()
                .getApplicableErrorHandlers(statusCode);

        for (BillServiceErrorHandlerFactory factory : factories) {
            BillServiceErrorHandler errorHandler = factory.create(statusCode);
            DocumentStatusResult statusResult = errorHandler.getStatus(ticket, config);
            if (statusResult != null) {
                return statusResult;
            }
        }

        return null;
    }

    /**
     * @param file   archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static SendSummaryResult sendSummary(File file, ServiceConfig config) throws IOException {
        return sendSummary(file.toPath(), config);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static SendSummaryResult sendSummary(Path path, ServiceConfig config) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static SendSummaryResult sendSummary(String fileName, byte[] file, ServiceConfig config) throws IOException {
        if (fileName.endsWith(".xml")) {
            file = ZipBuilder.createZipInMemory()
                    .add(file)
                    .path(fileName)
                    .save()
                    .toBytes();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        }

        String ticket = null;
        try {
            ticket = BillServiceWrapper.sendSummary(fileName, file, null, config);
        } catch (SOAPFaultException e) {
            Set<BillServiceErrorHandlerFactory> factories = BillServiceErrorHandlerFactoryManager
                    .getInstance()
                    .getApplicableErrorHandlers(e);
            for (BillServiceErrorHandlerFactory factory : factories) {
                BillServiceErrorHandler errorHandler = factory.create(e);
                ticket = errorHandler.sendSummary(fileName, file, null, config);
                if (ticket != null) {
                    break;
                }
            }
            if (ticket == null) {
                throw WebServiceExceptionFactory.createWebServiceException(e);
            }
        }


        SendSummaryResult sendSummaryResult = new SendSummaryResult(ticket);


        try {
            DocumentStatusResult statusResult = getStatus(ticket, config);
            sendSummaryResult.setStatusResult(statusResult);
        } catch (SOAPFaultException e) {
            // Should be removed on development
            System.out.println(e);
        }


        return sendSummaryResult;
    }

    /**
     * @param file   archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static String sendPack(File file, ServiceConfig config) throws IOException {
        return sendPack(file.toPath(), config);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static String sendPack(Path path, ServiceConfig config) throws IOException {
        return sendPack(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. El archivo deberá ser un archivo .zip
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static String sendPack(String fileName, byte[] file, ServiceConfig config) {
        try {
            return BillServiceWrapper.sendPack(fileName, file, null, config);
        } catch (SOAPFaultException e) {
            Set<BillServiceErrorHandlerFactory> factories = BillServiceErrorHandlerFactoryManager
                    .getInstance()
                    .getApplicableErrorHandlers(e);
            for (BillServiceErrorHandlerFactory factory : factories) {
                BillServiceErrorHandler errorHandler = factory.create(e);
                String ticket = errorHandler.sendPack(fileName, file, null, config);
                if (ticket != null) {
                    return ticket;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        }
    }

}
