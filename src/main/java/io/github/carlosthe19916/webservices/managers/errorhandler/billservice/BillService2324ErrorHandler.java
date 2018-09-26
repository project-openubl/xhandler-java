package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.utils.CdrUtils;
import io.github.carlosthe19916.webservices.utils.Utils;
import io.github.carlosthe19916.webservices.wrappers.BillServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class BillService2324ErrorHandler extends AbstractBillServiceErrorHandler {

    private static final int ERROR_CODE = 2_324;

    public BillService2324ErrorHandler(int errorCode) {
        boolean isInRange = errorCode == ERROR_CODE;
        if (!isInRange) {
            throw new IllegalArgumentException("Can not create Error 1033 with code:" + errorCode);
        }
    }

    public BillService2324ErrorHandler(SOAPFaultException exception) {
        this(Utils.getErrorCode(exception).orElse(-1));
    }

    @Override
    public DocumentStatusResult getStatus(String ticket, ServiceConfig config) {
        StatusResponse statusResponse = BillServiceWrapper.getStatus(ticket, config);
        try {
            DocumentStatusResult statusResult = CdrUtils.getInfoFromCdrZip(statusResponse.getContent());
            statusResult.setStatus(DocumentStatusResult.Status.ACEPTADO);
            return statusResult;
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }
}
