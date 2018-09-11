package io.github.carlosthe19916.webservices.exceptions;

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

}
