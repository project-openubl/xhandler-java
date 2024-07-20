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
public class Descuento {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Catalogo 53")
    private String tipoDescuento;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal factor;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal monto;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal montoBase;

}
