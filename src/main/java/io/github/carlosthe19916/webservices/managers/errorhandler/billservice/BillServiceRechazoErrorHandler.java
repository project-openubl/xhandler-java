package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.utils.Util;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceRechazoErrorHandler extends AbstractBillServiceErrorHandler {

    @Override
    public boolean test(SOAPFaultException e) {
        Integer errorCode = Util.getErrorCode(e).orElse(-1);
        return errorCode >= 2_000 && errorCode < 4_000;
    }

}
