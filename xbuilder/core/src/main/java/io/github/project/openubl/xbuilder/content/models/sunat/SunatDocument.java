package io.github.project.openubl.xbuilder.content.models.sunat;

import io.github.project.openubl.xbuilder.content.models.common.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SunatDocument extends Document {

    /**
     * Numero de comprobante emitido para la fecha de emision dada
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "99999999")
    private Integer numero;

    /**
     * Fecha de emisión de los comprobantes dados de baja. Ejemplo 2022-12-25 (YYYY-MM-SS)
     */
    @Schema(description = "Format: \"YYYY-MM-SS\". Ejemplo: 2022-12-25", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaEmisionComprobantes;
}
