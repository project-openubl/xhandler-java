package io.github.carlosthe19916.webservices.models;

public class CdrResponseBean {

    private final String responseCode;
    private final String description;

    public CdrResponseBean(String responseCode, String description) {
        this.responseCode = responseCode;
        this.description = description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getDescription() {
        return description;
    }

}
