package io.github.carlosthe19916.webservices.models;

public class BillServiceResult {

    private final Status status;
    private final byte[] cdr;
    private final String description;

    public BillServiceResult(Status status, byte[] cdr, String description) {
        this.status = status;
        this.cdr = cdr;
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public byte[] getCdr() {
        return cdr;
    }

    public String getDescription() {
        return description;
    }

    public static enum Status {
        ACEPTADO,
        RECHAZADO,
        BAJA
    }
}
