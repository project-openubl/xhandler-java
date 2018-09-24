package io.github.carlosthe19916.webservices.managers.errorhandler;

import javax.xml.ws.soap.SOAPFaultException;

public interface BillServiceErrorHandlerFactory extends SUNATErrorHandlerFactory {

    BillServiceErrorHandler create(SOAPFaultException e);

}
