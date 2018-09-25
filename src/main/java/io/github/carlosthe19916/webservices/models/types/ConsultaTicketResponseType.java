package io.github.carlosthe19916.webservices.models.types;

import java.util.Optional;
import java.util.stream.Stream;

public enum ConsultaTicketResponseType {

    PROCESO_CORRECTAMENTE(0, "Procesó correctamente"),
    EN_PROCESO(98, "En proceso"),
    PROCESO_CON_ERRORES(99, "Procesó con errores");

    private final int code;
    private final String description;

    ConsultaTicketResponseType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<ConsultaTicketResponseType> fromCode(int code) {
        return Stream.of(ConsultaTicketResponseType.values()).filter(p -> p.getCode() == code).findFirst();
    }

}
