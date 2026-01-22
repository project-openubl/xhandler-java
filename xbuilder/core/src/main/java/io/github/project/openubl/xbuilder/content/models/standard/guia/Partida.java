package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partida {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String ubigeo;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccion;

    /**
     * Código de establecimiento del punto de partida
     */
    @Schema(description = "Código de local anexo de partida")
    private String codigoLocal;

    /**
     * RUC asociado al punto de partida
     */
    @Schema(description = "RUC asociado al punto de partida")
    private String ruc;
}
