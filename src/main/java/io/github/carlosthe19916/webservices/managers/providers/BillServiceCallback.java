package io.github.carlosthe19916.webservices.managers.providers;

public interface BillServiceCallback {

    void onSuccess(int code, String description, byte[] cdr);

    void onError(int code, String description, byte[] cdr);

    void onProcess(int code, String description);

    void onException(int code, String description);

}
