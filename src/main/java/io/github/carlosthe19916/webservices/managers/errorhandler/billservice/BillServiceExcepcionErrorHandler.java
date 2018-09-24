package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceExcepcionErrorHandler extends AbstractBillServiceErrorHandler {

    private final SOAPFaultException exception;

    public BillServiceExcepcionErrorHandler(SOAPFaultException exception) {
        this.exception = exception;
    }

}
