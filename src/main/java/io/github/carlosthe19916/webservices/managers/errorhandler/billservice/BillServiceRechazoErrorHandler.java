package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceRechazoErrorHandler extends AbstractBillServiceErrorHandler {

    private final SOAPFaultException exception;

    public BillServiceRechazoErrorHandler(SOAPFaultException exception) {
        this.exception = exception;
    }

}
