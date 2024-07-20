package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class TotalImporte {

    @Schema(minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal importe;

    @Schema(minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal importeSinImpuestos;

    @Schema(minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal importeConImpuestos;
}
