package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.utils.Util;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceObservacionesErrorHandler extends AbstractBillServiceErrorHandler {

    @Override
    public boolean test(SOAPFaultException e) {
        return Util.getErrorCode(e).orElse(-1) >= 4_000;
    }

}
