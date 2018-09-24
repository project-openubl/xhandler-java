package io.github.carlosthe19916.webservices.managers.errorhandler;

import io.github.carlosthe19916.webservices.models.BillServiceResult;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public interface BillServiceErrorHandler {

    BillServiceResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config);

    service.sunat.gob.pe.billservice.StatusResponse getStatus(String ticket, ServiceConfig config);

    String sendSummary(String fileName, byte[] zipFile, String partyType, ServiceConfig config);

    String sendPack(String fileName, byte[] zipFile, String partyType, ServiceConfig config);

}
