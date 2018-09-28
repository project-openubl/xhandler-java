package io.github.carlosthe19916.webservices.providers.errors;

import io.github.carlosthe19916.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.utils.SunatErrors;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public class ErrorRechazoBillServiceProvider extends AbstractErrorBillServiceProvider {

    private final BillServiceModel previousResult;
    private final Integer exceptionCode;

    public ErrorRechazoBillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public ErrorRechazoBillServiceProvider(BillServiceModel previousResult, Integer exceptionCode) {
        this.previousResult = previousResult;
        this.exceptionCode = exceptionCode;
    }

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {
        BillServiceModel result = new BillServiceModel();

        if (previousResult == null) {
            String errorMessage = SunatErrors.getInstance().get(exceptionCode);

            result.setStatus(BillServiceModel.Status.RECHAZADO);
            result.setCode(exceptionCode);
            result.setDescription(errorMessage);
        } else {
            result.setCdr(previousResult.getCdr());
            result.setCode(previousResult.getCode());
            result.setStatus(previousResult.getStatus());
            result.setTicket(previousResult.getTicket());
            result.setDescription(previousResult.getDescription());
        }

        return result;
    }
}
