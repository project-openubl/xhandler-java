package io.github.carlosthe19916.webservices.managers.providers;

public class BillServiceModel {

    private Status status;
    private Integer code;
    private String description;
    private byte[] cdr;
    private String ticket;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCdr() {
        return cdr;
    }

    public void setCdr(byte[] cdr) {
        this.cdr = cdr;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public enum Status {
        ACEPTADO,
        RECHAZADO,
        BAJA,
        EXCEPCION,
        EN_PROCESO;

        public static Status fromCode(int code) {
            if (code >= 100 && code < 2_000) {
                return Status.EXCEPCION;
            } else if (code >= 2000 && code < 4000) {
                return Status.RECHAZADO;
            } else if (code >= 4000) {
                return Status.ACEPTADO;
            } else {
                return null;
            }
        }
    }

}
