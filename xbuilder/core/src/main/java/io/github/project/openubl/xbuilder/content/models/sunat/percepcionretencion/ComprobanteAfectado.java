package io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion;

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
public class ComprobanteAfectado {
    private String moneda;

    @Schema(description = "Catalogo 01")
    private String tipoComprobante;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serieNumero;

    @Schema(description = "Format: \"YYYY-MM-SS\". Ejemplo: 2022-12-25", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaEmision;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal importeTotal;
}
