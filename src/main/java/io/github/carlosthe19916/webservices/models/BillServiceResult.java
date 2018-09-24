package io.github.carlosthe19916.webservices.models;

public class BillServiceResult {

    private DocumentStatus status;
    private byte[] cdr;
    private int code;
    private String description;

    public BillServiceResult(DocumentStatus status, byte[] cdr, int code, String description) {
        this.status = status;
        this.cdr = cdr;
        this.code = code;
        this.description = description;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public byte[] getCdr() {
        return cdr;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public void setCdr(byte[] cdr) {
        this.cdr = cdr;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum DocumentStatus {
        ACEPTADO,
        RECHAZADO,
        BAJA,
        EXCEPCION
    }
}
