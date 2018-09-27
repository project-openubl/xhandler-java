package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public abstract class AbstractBillServiceErrorHandler implements BillServiceErrorHandler {

    protected DocumentStatusResult previousStatusResult;

    @Override
    public DocumentStatusResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        return null;
    }

    @Override
    public DocumentStatusResult getStatus(String ticket, ServiceConfig config) {
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
    public void setPreviousStatusResult(DocumentStatusResult previousStatusResult) {
        this.previousStatusResult = previousStatusResult;
    }

}
