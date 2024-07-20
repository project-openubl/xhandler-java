package io.github.project.openubl.xsender.models;

public enum Status {
    ACEPTADO,
    RECHAZADO,
    BAJA,
    EXCEPCION,
    EN_PROCESO,
    UNKNOWN;

    public static Status fromCode(int code) {
        if (code == 0) {
            return Status.ACEPTADO;
        } else if (code == 98) {
            return Status.EN_PROCESO;
        } else if (code == 99) {
            return Status.EXCEPCION;
        } else if (code >= 100 && code < 2_000) {
            return Status.EXCEPCION;
        } else if (code >= 2000 && code < 4000) {
            return Status.RECHAZADO;
        } else if (code >= 4000) {
            return Status.ACEPTADO;
        } else {
            return Status.UNKNOWN;
        }
    }
}
