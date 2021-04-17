package io.github.project.openubl.xsender.discovery;

public class FileDeliveryTarget {

    private final String url;
    private final Method method;

    public FileDeliveryTarget(String url, Method method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public enum Method {
        SEND_BILL,
        SEND_SUMMARY,
        SEND_PACK,
    }
}
