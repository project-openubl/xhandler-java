package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FormaDePago {

    @Schema(description = "CREDITO o CONTADO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipo;

    @Schema(description = "Monto total de pago", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal total;

    @Schema(description = "Cuotas de pago")
    @Singular
    private List<CuotaDePago> cuotas;
}
