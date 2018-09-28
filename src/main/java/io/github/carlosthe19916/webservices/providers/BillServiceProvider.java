package io.github.carlosthe19916.webservices.providers;

import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public interface BillServiceProvider {

    BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config);

    BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config);

    BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config, BillServiceCallback callback, long delay);

    BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config);

    BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config, BillServiceCallback callback, long delay);

    BillServiceModel getStatus(String ticket, ServiceConfig config);

}
