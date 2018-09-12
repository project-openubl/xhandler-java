package io.github.carlosthe19916.webservices.exceptions;

import javax.xml.ws.soap.SOAPFaultException;

public abstract class AbstractWebServiceException extends SOAPFaultException {

    protected final SOAPFaultException exception;

    public AbstractWebServiceException(SOAPFaultException exception) {
        super(exception.getFault());
        this.exception = exception;
    }

    public SOAPFaultException getException() {
        return exception;
    }

}
