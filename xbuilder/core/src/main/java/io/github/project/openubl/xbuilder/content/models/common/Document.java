package io.github.project.openubl.xbuilder.content.models.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class Document {

    /**
     * Moneda en la que se emite el comprobante
     */
    @Schema(minLength = 3, maxLength = 3)
    private String moneda;

    /**
     * Fecha de emisión del comprobante. Ejemplo 2022-12-25 (YYYY-MM-SS)
     */
    @Schema(description = "Format: \"YYYY-MM-SS\". Ejemplo: 2022-12-25", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaEmision;

    /**
     * Proveedor del bien o servicio
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Proveedor proveedor;

    /**
     * Persona que firma electrónicamente el comprobante. Si es NULL los datos del proveedor son usados.
     */
    @Schema(description = "Persona que firma el comprobante. Si NULL los datos del proveedor son usados.")
    private Firmante firmante;

}
