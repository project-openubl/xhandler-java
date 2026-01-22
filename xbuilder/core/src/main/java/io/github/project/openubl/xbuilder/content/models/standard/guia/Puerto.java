package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para puerto o aeropuerto de embarque/desembarque.
 * Basado en el modelo Puerto de greenter.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Puerto {

    /**
     * Código del puerto (Catálogo 63) o aeropuerto (Catálogo 64)
     */
    @Schema(description = "Código del puerto (Cat. 63) o aeropuerto (Cat. 64)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;

    /**
     * Nombre del puerto o aeropuerto
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}
