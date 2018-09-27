package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.utils.Utils;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import javax.xml.ws.soap.SOAPFaultException;

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
        if (previousStatusResult != null) {
            previousStatusResult.setStatus(DocumentStatusResult.Status.ACEPTADO);
        }
        return previousStatusResult;
    }
}
