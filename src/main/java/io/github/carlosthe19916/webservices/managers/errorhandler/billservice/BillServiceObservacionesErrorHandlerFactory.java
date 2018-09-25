package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandlerFactory;
import io.github.carlosthe19916.webservices.utils.Util;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceObservacionesErrorHandlerFactory implements BillServiceErrorHandlerFactory {

    @Override
    public BillServiceErrorHandler create(SOAPFaultException exception) {
        return new BillServiceObservacionesErrorHandler(exception);
    }

    @Override
    public boolean test(SOAPFaultException exception) {
        return Util.getErrorCode(exception).orElse(-1) >= 4_000;
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
