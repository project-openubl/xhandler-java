package io.github.carlosthe19916.webservices.exceptions;

import io.github.carlosthe19916.webservices.managers.SUNATCodigoErrores;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class ValidationWebServiceException extends AbstractWebServiceException {

    public ValidationWebServiceException(SOAPFaultException exception) {
        super(exception);
    }

    public Integer getSUNATErrorCode() {
        return Utils.getErrorCode(exception).orElse(-1);
    }

    public String getSUNATErrorMessage() {
        Integer errorCode = getSUNATErrorCode();
        return SUNATCodigoErrores.getInstance().get(errorCode);
    }

    public String getSUNATErrorMessage(int maxLength) {
        Integer errorCode = getSUNATErrorCode();
        return SUNATCodigoErrores.getInstance().getWithMaxLength(errorCode, maxLength);
    }

}
