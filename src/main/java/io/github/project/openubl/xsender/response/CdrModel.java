package io.github.project.openubl.xsender.response;

import java.util.List;

public class CdrModel {

    private final Integer responseCode;
    private final String description;
    private final List<String> notes;

    public CdrModel(Integer responseCode, String description, List<String> notes) {
        this.responseCode = responseCode;
        this.description = description;
        this.notes = notes;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getNotes() {
        return notes;
    }
}
