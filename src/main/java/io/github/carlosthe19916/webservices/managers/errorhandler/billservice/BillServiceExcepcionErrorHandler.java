package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.SUNATCodigoErrores;
import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.utils.Util;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceExcepcionErrorHandler extends AbstractBillServiceErrorHandler {

    private final int errorCode;

    public BillServiceExcepcionErrorHandler(int errorCode) {
        boolean isInRange = errorCode >= 100 && errorCode < 2_000;
        if (!isInRange) {
            throw new IllegalArgumentException("Can not create Exception with code:" + errorCode);
        }
        this.errorCode = errorCode;
    }

    public BillServiceExcepcionErrorHandler(SOAPFaultException exception) {
        this(Util.getErrorCode(exception).orElse(-1));
    }

    @Override
    public DocumentStatusResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        String message = SUNATCodigoErrores.getInstance().get(errorCode);
        return new DocumentStatusResult(DocumentStatusResult.Status.EXCEPCION, null, errorCode, message);
    }

}
