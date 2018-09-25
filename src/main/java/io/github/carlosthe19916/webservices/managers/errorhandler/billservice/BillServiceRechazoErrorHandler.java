package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.SUNATCodigoErrores;
import io.github.carlosthe19916.webservices.models.BillServiceResult;
import io.github.carlosthe19916.webservices.utils.Util;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceRechazoErrorHandler extends AbstractBillServiceErrorHandler {

    private final int errorCode;
    private final SOAPFaultException exception;

    public BillServiceRechazoErrorHandler(SOAPFaultException exception) {
        errorCode = Util.getErrorCode(exception).orElse(-1);
        boolean isInRange = errorCode >= 2_000 && errorCode < 4_000;
        if (!isInRange) {
            throw new IllegalArgumentException("Can not create Rechazado with code:" + errorCode);
        }
        this.exception = exception;
    }

    @Override
    public BillServiceResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        String message = SUNATCodigoErrores.getInstance().get(errorCode);
        return new BillServiceResult(BillServiceResult.DocumentStatus.RECHAZADO, null, errorCode, message);
    }

}
