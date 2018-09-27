package io.github.carlosthe19916.webservices.managers.providers.errors;

import io.github.carlosthe19916.webservices.managers.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;

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
}
