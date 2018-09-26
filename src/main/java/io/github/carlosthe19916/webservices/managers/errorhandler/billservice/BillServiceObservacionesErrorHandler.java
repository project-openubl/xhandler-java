package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.SUNATCodigoErrores;
import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.utils.Utils;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceObservacionesErrorHandler extends AbstractBillServiceErrorHandler {

    private final int errorCode;

    public BillServiceObservacionesErrorHandler(int errorCode) {
        boolean isInRange = errorCode >= 4_000;
        if (!isInRange) {
            throw new IllegalArgumentException("Can not create Observado with code:" + errorCode);
        }
        this.errorCode = errorCode;
    }

    public BillServiceObservacionesErrorHandler(SOAPFaultException exception) {
        this(Utils.getErrorCode(exception).orElse(-1));
    }

    @Override
    public DocumentStatusResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        String message = SUNATCodigoErrores.getInstance().get(errorCode);
        return new DocumentStatusResult(DocumentStatusResult.Status.ACEPTADO, null, errorCode, message);
    }

}
