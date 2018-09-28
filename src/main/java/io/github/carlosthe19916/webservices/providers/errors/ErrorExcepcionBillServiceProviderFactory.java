package io.github.carlosthe19916.webservices.providers.errors;

import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.providers.ErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.ErrorBillServiceProviderFactory;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class ErrorExcepcionBillServiceProviderFactory implements ErrorBillServiceProviderFactory {

    static final int MIN_ERROR_CODE = 100;
    static final int MAX_ERROR_CODE = 1_999;

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode) {
        return new ErrorExcepcionBillServiceProvider(exceptionCode);
    }

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode, BillServiceModel previousResult) {
        return new ErrorExcepcionBillServiceProvider(exceptionCode, previousResult);
    }

    @Override
    public boolean isSupported(int errorCode) {
        return errorCode >= MIN_ERROR_CODE && errorCode <= MAX_ERROR_CODE;
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
