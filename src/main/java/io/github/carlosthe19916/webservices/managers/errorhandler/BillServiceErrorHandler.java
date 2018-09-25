package io.github.carlosthe19916.webservices.managers.errorhandler;

import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public interface BillServiceErrorHandler {

    DocumentStatusResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config);

    DocumentStatusResult getStatus(String ticket, ServiceConfig config);

    String sendSummary(String fileName, byte[] zipFile, String partyType, ServiceConfig config);

    String sendPack(String fileName, byte[] zipFile, String partyType, ServiceConfig config);

}
