package io.github.carlosthe19916.webservices.managers.providers.errors;

import io.github.carlosthe19916.webservices.managers.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public class Error2324BillServiceProvider extends AbstractErrorBillServiceProvider {

    private final Integer exceptionCode;
    private final BillServiceModel previousResult;

    public Error2324BillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public Error2324BillServiceProvider(Integer exceptionCode, BillServiceModel previousResult) {
        this.exceptionCode = exceptionCode;
        this.previousResult = previousResult;
    }

    @Override
    public BillServiceModel getStatus(String ticket, ServiceConfig config) {
        if (previousResult != null && previousResult.getCdr() != null) {
            BillServiceModel result = new BillServiceModel();

            // Copy info
            result.setTicket(previousResult.getTicket());
            result.setDescription(previousResult.getDescription());
            result.setCode(previousResult.getCode());
            result.setCdr(previousResult.getCdr());

            // Change status
            result.setStatus(BillServiceModel.Status.ACEPTADO);
            return result;
        }
        return null;
    }
}
