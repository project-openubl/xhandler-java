package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public class Error2335BillServiceProvider extends AbstractErrorBillServiceProvider {

    private final Integer exceptionCode;
    private final BillServiceModel previousResult;

    public Error2335BillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public Error2335BillServiceProvider(Integer exceptionCode, BillServiceModel previousResult) {
        this.exceptionCode = exceptionCode;
        this.previousResult = previousResult;
    }

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {
        BillServiceModel result = new BillServiceModel();

        result.setCode(2335);
        result.setDescription("My mock message");
        result.setStatus(BillServiceModel.Status.RECHAZADO);
        return result;
    }
}
