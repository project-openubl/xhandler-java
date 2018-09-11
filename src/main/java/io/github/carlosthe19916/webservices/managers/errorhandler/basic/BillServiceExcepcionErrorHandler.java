package io.github.carlosthe19916.webservices.managers.errorhandler.basic;

import io.github.carlosthe19916.webservices.managers.errorhandler.AbstractErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.models.BillServiceResult;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import service.sunat.gob.pe.billservice.StatusResponse;

import javax.xml.ws.soap.SOAPFaultException;

public class BillServiceExcepcionErrorHandler extends AbstractErrorHandler implements BillServiceErrorHandler {

    @Override
    public BillServiceResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        return null;
    }

    @Override
    public StatusResponse getStatus(String ticket, ServiceConfig config) {
        return null;
    }

    @Override
    public String sendSummary(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        return null;
    }

    @Override
    public String sendPack(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean test(SOAPFaultException e) {
        Integer errorCode = getErrorCode(e).orElse(-1);
        return errorCode >= 1 && errorCode < 2_000;
    }
}
