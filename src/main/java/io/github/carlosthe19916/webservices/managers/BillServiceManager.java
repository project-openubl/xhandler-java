package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.exceptions.WebServiceExceptionFactory;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandlerManager;
import io.github.carlosthe19916.webservices.models.BillServiceResult;
import io.github.carlosthe19916.webservices.utils.Util;
import io.github.carlosthe19916.webservices.wrappers.BillServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import jodd.io.ZipBuilder;
import org.w3c.dom.Document;

import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.*;
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
    public static BillServiceResult sendBill(File file, ServiceConfig config) throws IOException {
        return sendBill(file.toPath(), config);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceResult sendBill(Path path, ServiceConfig config) throws IOException {
        return sendBill(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static BillServiceResult sendBill(String fileName, byte[] file, ServiceConfig config) throws IOException {
        if (fileName.endsWith(".xml")) {
            file = ZipBuilder.createZipInMemory()
                    .add(file)
                    .path(fileName)
                    .save()
                    .toBytes();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        }

        try {
            byte[] cdrZip = BillServiceWrapper.sendBill(fileName, file, null, config);
//            byte[] cdrXml = Util.getFirstXmlFileFromZip(cdrZip);
//            Document cdrDocument = Util.getDocumentFromBytes(cdrXml);
//
//            XPathFactory xPathFactory = XPathFactory.newInstance();
//            XPath xPath = xPathFactory.newXPath();
//            try {
//                XPathExpression xPathExpression = xPath.compile("/");
//                String  description = (String) xPathExpression.evaluate(cdrDocument, XPathConstants.STRING);
//                String  codigo = (String) xPathExpression.evaluate(cdrDocument, XPathConstants.STRING);
//            } catch (XPathExpressionException e) {
//                e.printStackTrace();
//            }
//
//            return new BillServiceResult(BillServiceResult.DocumentStatus.ACEPTADO, cdrZip, null);
            return null;
        } catch (SOAPFaultException e) {
            Set<BillServiceErrorHandler> billServiceErrorHandlers = BillServiceErrorHandlerManager.getInstance().getApplicableErrorHandlers(e);
            for (BillServiceErrorHandler errorHandler : billServiceErrorHandlers) {
                BillServiceResult result = errorHandler.sendBill(fileName, file, null, config);
                if (result != null) {
                    return result;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        }
    }

    /**
     * @param ticket numero de ticket a ser consultado
     * @param config Credenciales y URL de destino de la petición
     */
    public static service.sunat.gob.pe.billservice.StatusResponse getStatus(String ticket, ServiceConfig config) {
        try {
            return BillServiceWrapper.getStatus(ticket, config);
        } catch (SOAPFaultException e) {
            Set<BillServiceErrorHandler> billServiceErrorHandlers = BillServiceErrorHandlerManager.getInstance().getApplicableErrorHandlers(e);
            for (BillServiceErrorHandler errorHandler : billServiceErrorHandlers) {
                service.sunat.gob.pe.billservice.StatusResponse statusResponse = errorHandler.getStatus(ticket, config);
                if (statusResponse != null) {
                    return statusResponse;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        }
    }

    /**
     * @param file   archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static String sendSummary(File file, ServiceConfig config) throws IOException {
        return sendSummary(file.toPath(), config);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static String sendSummary(Path path, ServiceConfig config) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static String sendSummary(String fileName, byte[] file, ServiceConfig config) throws IOException {
        if (fileName.endsWith(".xml")) {
            file = ZipBuilder.createZipInMemory()
                    .add(file)
                    .path(fileName)
                    .save()
                    .toBytes();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        }

        try {
            return BillServiceWrapper.sendSummary(fileName, file, null, config);
        } catch (SOAPFaultException e) {
            Set<BillServiceErrorHandler> billServiceErrorHandlers = BillServiceErrorHandlerManager.getInstance().getApplicableErrorHandlers(e);
            for (BillServiceErrorHandler errorHandler : billServiceErrorHandlers) {
                String ticket = errorHandler.sendSummary(fileName, file, null, config);
                if (ticket != null) {
                    return ticket;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        }
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
            Set<BillServiceErrorHandler> billServiceErrorHandlers = BillServiceErrorHandlerManager.getInstance().getApplicableErrorHandlers(e);
            for (BillServiceErrorHandler errorHandler : billServiceErrorHandlers) {
                String ticket = errorHandler.sendPack(fileName, file, null, config);
                if (ticket != null) {
                    return ticket;
                }
            }
            throw WebServiceExceptionFactory.createWebServiceException(e);
        }
    }

}
