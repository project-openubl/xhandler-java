package io.github.carlosthe19916.webservices.providers;

import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public abstract class AbstractErrorBillServiceProvider implements ErrorBillServiceProvider {

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {
        return null;
    }

    @Override
    public BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config) {
        return null;
    }

    @Override
    public BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config, BillServiceCallback callback, long delay) {
        return null;
    }

    @Override
    public BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config) {
        return null;
    }

    @Override
    public BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config, BillServiceCallback callback, long delay) {
        return null;
    }

    @Override
    public BillServiceModel getStatus(String ticket, ServiceConfig config) {
        return null;
    }

}
