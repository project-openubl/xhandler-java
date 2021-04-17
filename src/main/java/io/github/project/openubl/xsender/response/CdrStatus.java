package io.github.project.openubl.xsender.response;

import java.util.Optional;

public enum CrdStatus {

    ACEPTADO,
    RECHAZADO,
    BAJA,
    EXCEPCION,
    EN_PROCESO;

    public static Optional<CrdStatus> fromCode(int code) {
        if (code == 0) {
            return Optional.of(CrdStatus.ACEPTADO);
        } else if (code >= 100 && code < 2_000) {
            return Optional.of(CrdStatus.EXCEPCION);
        } else if (code >= 2000 && code < 4000) {
            return Optional.of(CrdStatus.RECHAZADO);
        } else if (code >= 4000) {
            return Optional.of(CrdStatus.ACEPTADO);
        } else {
            return Optional.empty();
        }
    }
}
