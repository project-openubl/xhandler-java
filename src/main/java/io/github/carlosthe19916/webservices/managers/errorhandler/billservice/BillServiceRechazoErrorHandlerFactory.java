package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandlerFactory;
import io.github.carlosthe19916.webservices.utils.Util;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceRechazoErrorHandlerFactory implements BillServiceErrorHandlerFactory {

    @Override
    public BillServiceErrorHandler create(SOAPFaultException exception) {
        if (!test(exception)) {
            throw new IllegalArgumentException("Exception type not allowed");
        }
        return new BillServiceRechazoErrorHandler(exception);
    }

    @Override
    public boolean test(SOAPFaultException exception) {
        Integer errorCode = Util.getErrorCode(exception).orElse(-1);
        return errorCode >= 2_000 && errorCode < 4_000;
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
