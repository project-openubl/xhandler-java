package io.github.carlosthe19916.webservices.providers.errors;

import io.github.carlosthe19916.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.utils.SunatErrors;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public class ErrorExcepcionBillServiceProvider extends AbstractErrorBillServiceProvider {

    private final Integer exceptionCode;
    private final BillServiceModel previousResult;

    public ErrorExcepcionBillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public ErrorExcepcionBillServiceProvider(Integer exceptionCode, BillServiceModel previousResult) {
        this.exceptionCode = exceptionCode;
        this.previousResult = previousResult;
    }

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {
        String errorMessage = SunatErrors.getInstance().get(exceptionCode);

        BillServiceModel result = new BillServiceModel();
        result.setStatus(BillServiceModel.Status.EXCEPCION);
        result.setCode(exceptionCode);
        result.setDescription(errorMessage);

        return result;
    }
}
