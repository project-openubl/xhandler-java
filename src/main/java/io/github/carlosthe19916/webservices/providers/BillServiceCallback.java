package io.github.carlosthe19916.webservices.providers;

import javax.xml.ws.soap.SOAPFaultException;

public interface BillServiceCallback {

    void onSuccess(int code, String description, byte[] cdr);

    void onError(int code, String description, byte[] cdr);

    void onProcess(int code, String description);

    void onException(int code, String description);

    void onThrownException(SOAPFaultException exception);

}
