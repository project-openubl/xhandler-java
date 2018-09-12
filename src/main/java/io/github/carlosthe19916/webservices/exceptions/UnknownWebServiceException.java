package io.github.carlosthe19916.webservices.exceptions;

import javax.xml.ws.soap.SOAPFaultException;

public class UnknownWebServiceException extends AbstractWebServiceException {

    public UnknownWebServiceException(SOAPFaultException exception) {
        super(exception);
    }

}
