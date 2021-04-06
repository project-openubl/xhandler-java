package io.github.project.openubl.xsender;

public class CompanyConfig {

    private String username;
    private String password;

    private String invoiceUrl;
    private String perceptionUrl;
    private String despatchUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getPerceptionUrl() {
        return perceptionUrl;
    }

    public void setPerceptionUrl(String perceptionUrl) {
        this.perceptionUrl = perceptionUrl;
    }

    public String getDespatchUrl() {
        return despatchUrl;
    }

    public void setDespatchUrl(String despatchUrl) {
        this.despatchUrl = despatchUrl;
    }

}
