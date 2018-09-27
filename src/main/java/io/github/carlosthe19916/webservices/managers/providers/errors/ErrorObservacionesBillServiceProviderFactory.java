package io.github.carlosthe19916.webservices.managers.providers.errors;

import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.managers.providers.ErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.managers.providers.ErrorBillServiceProviderFactory;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class ErrorObservacionesBillServiceProviderFactory implements ErrorBillServiceProviderFactory {

    static final int MIN_ERROR_CODE = 4_000;

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode) {
        return new ErrorObservacionesBillServiceProvider(exceptionCode);
    }

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode, BillServiceModel previousResult) {
        return new ErrorObservacionesBillServiceProvider(exceptionCode, previousResult);
    }

    @Override
    public boolean isSupported(int errorCode) {
        return errorCode >= MIN_ERROR_CODE;
    }

    @Override
    public boolean isSupported(SOAPFaultException exception) {
        return isSupported(Utils.getErrorCode(exception).orElse(-1));
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
