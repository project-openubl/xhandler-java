package io.github.carlosthe19916.webservices.managers.providers.errors;

import io.github.carlosthe19916.webservices.managers.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;

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

}
