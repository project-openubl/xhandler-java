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

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "Tasa de IGV del comprobante. Ejemplo: 0.18")
    private BigDecimal tasaIgv;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "ICB del comprobante")
    private BigDecimal icb;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "ISC del comprobante")
    private BigDecimal isc;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "IVAP del comprobante")
    private BigDecimal ivap;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "Otros tributos del comprobante")
    private BigDecimal otros;
}
