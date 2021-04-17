package io.github.project.openubl.xsender;

public class VerifyTicketResponse {

    private String statusCode;
    private byte[] cdr;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getCdr() {
        return cdr;
    }

    public void setCdr(byte[] cdr) {
        this.cdr = cdr;
    }
}
