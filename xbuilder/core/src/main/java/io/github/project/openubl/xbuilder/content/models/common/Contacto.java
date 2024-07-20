package io.github.project.openubl.xbuilder.content.models.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de contacto
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {

    /**
     * Número telefónico
     */
    @Schema(description = "Número telefónico")
    private String telefono;

    /**
     * Correo electrónico
     */
    @Schema(description = "Correo electrónico")
    private String email;
}
