package io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Retention extends BasePercepcionRetencion {
    /**
     * Serie del comprobante
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 4, pattern = "^[R|r].*$")
    private String serie;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Catalog 23")
    private String tipoRegimen;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal importeTotalRetenido;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal importeTotalPagado;
}
