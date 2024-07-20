package io.github.project.openubl.xbuilder.content.models.common;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cliente de la operación.
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    /**
     * Tipo de documento de identidad del cliente.
     * <p>
     * Catalogo 06.
     * <p>
     * Valores válidos: {@link Catalog6}
     */
    @Schema(description = "Catalogo 06", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumentoIdentidad;

    /**
     * Número de documento de identidad del cliente.
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroDocumentoIdentidad;

    /**
     * Nombre del cliente. Si el cliente es personal natural entonces
     * es el nombre y apellidos de la persona; si el cliente es una persona jurídica
     * entonces es la razón social de la empresa.
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    /**
     * Dirección del cliente
     */
    private Direccion direccion;

    /**
     * Datos de contacto del cliente
     */
    private Contacto contacto;
}
