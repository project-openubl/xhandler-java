package io.github.carlosthe19916.webservices.models;

public class BillServiceResult {

    private final DocumentStatus status;
    private final byte[] cdr;
    private final int code;
    private final String description;

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

    public enum DocumentStatus {
        ACEPTADO,
        RECHAZADO,
        BAJA,
        VUELVA_A_INTENTAR
    }
}
