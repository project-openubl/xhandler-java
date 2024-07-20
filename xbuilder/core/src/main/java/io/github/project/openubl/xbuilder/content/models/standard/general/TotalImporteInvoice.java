package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TotalImporteInvoice extends TotalImporte {

    @Schema(minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal anticipos;

    @Schema(minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal descuentos;
}
