package io.github.carlosthe19916.webservices.providers;

import javax.xml.ws.soap.SOAPFaultException;

public interface ErrorBillServiceProviderFactory {

    ErrorBillServiceProvider create(Integer exceptionCode);

    ErrorBillServiceProvider create(Integer exceptionCode, BillServiceModel previousResult);

    boolean isSupported(int errorCode);

    boolean isSupported(SOAPFaultException exception);

    int getPriority();

}
