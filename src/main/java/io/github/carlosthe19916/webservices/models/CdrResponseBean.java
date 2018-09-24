package io.github.carlosthe19916.webservices.models;

public class CdrResponseBean {

    private final Integer responseCode;
    private final String description;

    public CdrResponseBean(Integer responseCode, String description) {
        this.responseCode = responseCode;
        this.description = description;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getDescription() {
        return description;
    }

}
