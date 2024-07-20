package io.github.project.openubl.xbuilder.content.models.sunat.resumen;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComprobanteImpuestos {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "IGV del comprobante")
    private BigDecimal igv;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "ICB del comprobante")
    private BigDecimal icb;
}
