package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para el comprador en la guía de remisión.
 * Representa al adquiriente de los bienes cuando aplica.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comprador {

    /**
     * Tipo de documento de identidad (Catálogo 06)
     */
    @Schema(description = "Catalogo 06", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumentoIdentidad;

    /**
     * Número de documento de identidad
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroDocumentoIdentidad;

    /**
     * Razón social o nombre
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}
