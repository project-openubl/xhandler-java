package io.github.project.openubl.xsender.camel.routes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum TicketResponseType {
    PROCESO_CORRECTAMENTE(0, "Procesó correctamente"),
    EN_PROCESO(98, "En proceso"),
    PROCESO_CON_ERRORES(99, "Procesó con errores");

    private final int code;
    private final String description;

    public static Optional<TicketResponseType> getFromCode(int code) {
        return Arrays.stream(TicketResponseType.values()).filter(f -> f.getCode() == code).findFirst();
    }
}
