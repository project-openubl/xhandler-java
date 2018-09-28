package io.github.carlosthe19916.webservices.models;

import java.util.Optional;
import java.util.stream.Stream;

public enum TicketResponseType {

    PROCESO_CORRECTAMENTE(0, "Procesó correctamente"),
    EN_PROCESO(98, "En proceso"),
    PROCESO_CON_ERRORES(99, "Procesó con errores");

    private final int code;
    private final String description;

    TicketResponseType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
