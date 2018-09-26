package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandlerFactory;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class BillService2324ErrorHandlerFactory implements BillServiceErrorHandlerFactory {

    @Override
    public BillServiceErrorHandler create(int errorCode) {
        return new BillService2324ErrorHandler(errorCode);
    }

    @Override
    public BillServiceErrorHandler create(SOAPFaultException exception) {
        return new BillService2324ErrorHandler(exception);
    }

    @Override
    public boolean test(int errorCode) {
        return errorCode == 2_324;
    }

    @Override
    public boolean test(SOAPFaultException exception) {
        Integer errorCode = Utils.getErrorCode(exception).orElse(-1);
        return test(errorCode);
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
