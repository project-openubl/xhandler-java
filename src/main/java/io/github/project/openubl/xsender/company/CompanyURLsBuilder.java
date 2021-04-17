package io.github.project.openubl.xsender.company;

public final class CompanyURLsBuilder {

    private String invoice;
    private String perceptionRetention;
    private String despatch;

    private CompanyURLsBuilder() {
    }

    public static CompanyURLsBuilder aCompanyURLs() {
        return new CompanyURLsBuilder();
    }

    public CompanyURLsBuilder withInvoice(String invoice) {
        this.invoice = invoice;
        return this;
    }

    public CompanyURLsBuilder withPerceptionRetention(String perceptionRetention) {
        this.perceptionRetention = perceptionRetention;
        return this;
    }

    public CompanyURLsBuilder withDespatch(String despatch) {
        this.despatch = despatch;
        return this;
    }

    public CompanyURLs build() {
        return new CompanyURLs(invoice, perceptionRetention, despatch);
    }

}
