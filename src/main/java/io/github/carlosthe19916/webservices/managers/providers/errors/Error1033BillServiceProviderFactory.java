package io.github.carlosthe19916.webservices.managers.providers.errors;

import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.managers.providers.ErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.managers.providers.ErrorBillServiceProviderFactory;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class Error1033BillServiceProviderFactory implements ErrorBillServiceProviderFactory {

    static final int ERROR_CODE = 1_033;

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode) {
        return new Error1033BillServiceProvider(exceptionCode);
    }

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode, BillServiceModel previousResult) {
        return new Error1033BillServiceProvider(exceptionCode, previousResult);
    }

    @Override
    public boolean isSupported(int errorCode) {
        return errorCode == ERROR_CODE;
    }

    @Override
    public boolean isSupported(SOAPFaultException exception) {
        return Utils.getErrorCode(exception).orElse(-1) == ERROR_CODE;
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
