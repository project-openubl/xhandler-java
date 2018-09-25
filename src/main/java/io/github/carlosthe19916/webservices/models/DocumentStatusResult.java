package io.github.carlosthe19916.webservices.models;

public class DocumentStatusResult {

    private Status status;
    private byte[] cdr;
    private Integer code;
    private String description;

    public DocumentStatusResult(Status status, byte[] cdr, Integer code, String description) {
        this.status = status;
        this.cdr = cdr;
        this.code = code;
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public byte[] getCdr() {
        return cdr;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCdr(byte[] cdr) {
        this.cdr = cdr;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum Status {
        ACEPTADO,
        RECHAZADO,
        BAJA,
        EXCEPCION
    }
    
}
