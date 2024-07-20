package io.github.project.openubl.xbuilder.content.models.standard.general;

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
public class Percepcion {

    /**
     * Catalog53
     */
    @Schema(description = "Catalog 53", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipo;

    private BigDecimal montoBase; // importeSinImpuestos
    private BigDecimal porcentaje; // Establecido por el "tipo"
    private BigDecimal monto; // montoBase * porcentaje
    private BigDecimal montoTotal; // montoBase + monto
}
