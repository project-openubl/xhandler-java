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
public class SummaryPerception {

    @Schema(description = "Codigo de regimen de percepcion. Catalogo 22")
    private String codReg;

    @Schema(description = "Tasa de percepcion")
    private BigDecimal tasa;

    @Schema(description = "Monto base de percepcion")
    private BigDecimal mtoBase;

    @Schema(description = "Monto de percepcion")
    private BigDecimal mto;

    @Schema(description = "Monto total de percepcion")
    private BigDecimal mtoTotal;
}
