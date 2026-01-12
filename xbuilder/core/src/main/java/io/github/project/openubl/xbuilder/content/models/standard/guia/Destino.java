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
public class Destino {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String ubigeo;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccion;

    /**
     * Código de establecimiento del punto de llegada
     */
    @Schema(description = "Código de local anexo de llegada")
    private String codigoLocal;

    /**
     * RUC asociado al punto de llegada
     */
    @Schema(description = "RUC asociado al punto de llegada")
    private String ruc;
}
