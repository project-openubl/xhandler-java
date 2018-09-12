package io.github.carlosthe19916.webservices.exceptions;

import io.github.carlosthe19916.webservices.managers.SUNATCodigoErrores;
import io.github.carlosthe19916.webservices.utils.Util;

import javax.xml.ws.soap.SOAPFaultException;

public class SUNATWebServiceException extends SOAPFaultException {

    private final SOAPFaultException exception;

    public SUNATWebServiceException(SOAPFaultException exception) {
        super(exception.getFault());
        this.exception = exception;
    }

    public SOAPFaultException getException() {
        return exception;
    }

    public Integer getSUNATErrorCode() {
        return Util.getErrorCode(exception).orElse(-1);
    }

    public String getSUNATErrorMessage() {
        Integer sunatErrorCode = getSUNATErrorCode();
        return SUNATCodigoErrores.getInstance().get(sunatErrorCode);
    }

}
