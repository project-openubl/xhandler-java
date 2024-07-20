package io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion;

import io.github.project.openubl.xbuilder.content.models.common.TipoCambio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PercepcionRetencionOperacion {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Numero de cobro o pago")
    private Integer numeroOperacion;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Fecha en la que se realiza el cobro o pago")
    private LocalDate fechaOperacion;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Importe del cobro o pago")
    private BigDecimal importeOperacion;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private ComprobanteAfectado comprobante;

    private TipoCambio tipoCambio;
}
