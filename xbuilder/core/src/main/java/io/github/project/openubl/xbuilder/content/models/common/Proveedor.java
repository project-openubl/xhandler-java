package io.github.project.openubl.xbuilder.content.models.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persona que vende o presta un servicio
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    /**
     * Número de RUC de la persona jurídica
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 11, maxLength = 11, pattern = "[0-9]+")
    private String ruc;

    /**
     * Nombre comercial de la persona jurídica
     */
    private String nombreComercial;

    /**
     * Razón social de la persona jurídica
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String razonSocial;

    /**
     * Dirección de la persona jurídica
     */
    private Direccion direccion;

    /**
     * Contacto de la persona jurídica
     */
    private Contacto contacto;
}
