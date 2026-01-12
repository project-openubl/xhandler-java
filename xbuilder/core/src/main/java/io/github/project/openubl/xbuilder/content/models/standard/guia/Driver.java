package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para conductor/chofer de la guía de remisión.
 * Basado en el modelo Driver de greenter.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    /**
     * Tipo de conductor: "Principal" o "Secundario"
     */
    @Schema(description = "Tipo de conductor: Principal o Secundario")
    private String tipo;

    /**
     * Tipo de documento de identidad del chofer (Catálogo 06)
     */
    @Schema(description = "Catalogo 06", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumentoIdentidad;

    /**
     * Número de documento de identidad del chofer
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroDocumentoIdentidad;

    /**
     * Nombres del conductor
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombres;

    /**
     * Apellidos del conductor
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellidos;

    /**
     * Número de licencia de conducir
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String licencia;
}
