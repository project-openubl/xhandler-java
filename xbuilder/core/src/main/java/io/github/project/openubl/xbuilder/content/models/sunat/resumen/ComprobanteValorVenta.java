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
public class ComprobanteValorVenta {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal importeTotal;
    private BigDecimal otrosCargos;
    private BigDecimal gravado;
    private BigDecimal exonerado;
    private BigDecimal inafecto;
    private BigDecimal gratuito;
}
