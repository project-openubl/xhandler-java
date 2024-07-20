package io.github.project.openubl.xbuilder.content.models.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dirección
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    /**
     * Código de 6 dígitos que corresponden al Departamento, Provincia, y Distrito
     */
    @Schema(
            description = "Código de 6 dígitos que corresponden al Departamento, Provincia, y Distrito",
            minLength = 6,
            maxLength = 6,
            pattern = "[0-9]+"
    )
    private String ubigeo;

    /**
     * Código de cuatro dígitos asignado por SUNAT, que identifica al
     * establecimiento anexo. Dicho código se genera al momento la respectiva comunicación del
     * establecimiento. Tratándose del domicilio fiscal y en el caso de no poder determinar el lugar
     * de la venta, informar “0000”.
     */
    @Schema(
            description = "Código de cuatro dígitos asignado por SUNAT, que identifica al establecimiento anexo. Dicho código se genera al momento la respectiva comunicación del establecimiento. Tratándose del domicilio fiscal y en el caso de no poder determinar el lugar de la venta, informar \"0000\"",
            minLength = 4,
            maxLength = 4,
            pattern = "[0-9]+"
    )
    private String codigoLocal;

    /**
     * Nombre de la urbanización.
     */
    @Schema(description = "Nombre de la urbanización.")
    private String urbanizacion;

    /**
     * Nombre del Departamento o Región. Ejemplo: Ayacucho
     */
    @Schema(description = "Nombre del Departamento o Región. Ejemplo: Ayacucho")
    private String departamento;

    /**
     * Nombre de la Provincia. Ejemplo: Huamanga
     */
    @Schema(description = "Nombre de la Provincia. Ejemplo: Huamanga")
    private String provincia;

    /**
     * Nombre del Distrito. Ejemplo: Quinua
     */
    @Schema(description = "Nombre del Distrito. Ejemplo: Quinua")
    private String distrito;

    /**
     * Dirección. Ejemplo: Jirón las piedras 123
     */
    @Schema(description = "Dirección. Ejemplo: Jirón las piedras 123")
    private String direccion;

    /**
     * Código de 2 dígitos que corresponde al país a la que pertenece la dirección. Ejemplo: PE
     */
    @Schema(
            description = "Código de 2 dígitos que corresponde al país a la que pertenece la dirección. Ejemplo: PE",
            minLength = 2,
            maxLength = 2
    )
    private String codigoPais;
}
