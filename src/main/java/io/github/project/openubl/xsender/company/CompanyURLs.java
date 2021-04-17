package io.github.project.openubl.xsender.company;

public class CompanyURLs {

    private final String invoice;
    private final String perceptionRetention;
    private final String despatch;

    public CompanyURLs(String invoice, String perceptionRetention, String despatch) {
        this.invoice = invoice;
        this.perceptionRetention = perceptionRetention;
        this.despatch = despatch;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getPerceptionRetention() {
        return perceptionRetention;
    }

    public String getDespatch() {
        return despatch;
    }

}
