package io.github.project.openubl.xbuilder.content.models.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persona que firma electrónicamente el documento
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Firmante {

    /**
     * Número de RUC de la persona
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 11, maxLength = 11, pattern = "[0-9]+")
    private String ruc;

    /**
     * Razón social de la persona
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String razonSocial;
}
