package io.github.carlosthe19916.webservices.exceptions;

import io.github.carlosthe19916.webservices.utils.SunatErrors;
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
        return SunatErrors.getInstance().get(errorCode);
    }

    public String getSUNATErrorMessage(int maxLength) {
        Integer errorCode = getSUNATErrorCode();
        return SunatErrors.getInstance().getWithMaxLength(errorCode, maxLength);
    }

}
