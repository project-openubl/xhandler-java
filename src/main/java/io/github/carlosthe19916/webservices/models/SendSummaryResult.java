package io.github.carlosthe19916.webservices.models;

public class SendSummaryResult {

    private String ticket;
    private DocumentStatusResult statusResult;

    public SendSummaryResult(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public DocumentStatusResult getStatusResult() {
        return statusResult;
    }

    public void setStatusResult(DocumentStatusResult statusResult) {
        this.statusResult = statusResult;
    }

}
