package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.utils.Util;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import javax.xml.ws.soap.SOAPFaultException;

public class BillService2324ErrorHandler extends AbstractBillServiceErrorHandler {

    private final int errorCode;

    public BillService2324ErrorHandler(int errorCode) {
        boolean isInRange = errorCode == 2_324;
        if (!isInRange) {
            throw new IllegalArgumentException("Can not create Error 1033 with code:" + errorCode);
        }
        this.errorCode = errorCode;
    }

    public BillService2324ErrorHandler(SOAPFaultException exception) {
        this(Util.getErrorCode(exception).orElse(-1));
    }

    @Override
    public DocumentStatusResult getStatus(String ticket, ServiceConfig config) {
        return super.getStatus(ticket, config);
    }
}
