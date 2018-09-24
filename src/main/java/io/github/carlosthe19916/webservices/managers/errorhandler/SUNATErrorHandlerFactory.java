package io.github.carlosthe19916.webservices.managers.errorhandler;

import javax.xml.ws.soap.SOAPFaultException;

public interface SUNATErrorHandlerFactory {

    boolean test(SOAPFaultException e);

    int getPriority();

}
