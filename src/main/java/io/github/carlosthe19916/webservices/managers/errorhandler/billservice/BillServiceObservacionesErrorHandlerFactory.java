package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandlerFactory;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceObservacionesErrorHandlerFactory implements BillServiceErrorHandlerFactory {

    @Override
    public BillServiceErrorHandler create(int errorCode) {
        return new BillServiceObservacionesErrorHandler(errorCode);
    }

    @Override
    public BillServiceErrorHandler create(SOAPFaultException exception) {
        return new BillServiceObservacionesErrorHandler(exception);
    }

    @Override
    public boolean test(int errorCode) {
        return errorCode >= 4_000;
    }

    @Override
    public boolean test(SOAPFaultException exception) {
        return test(Utils.getErrorCode(exception).orElse(-1));
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
