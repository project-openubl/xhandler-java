package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Cuota de pago para Invoice
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuotaDePago {

    /**
     * Importe de la cuota
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", exclusiveMinimum = true)
    private BigDecimal importe;

    /**
     * Fecha de pago de la cuota
     */
    @Schema(description = "Ejemplo 2022-12-25", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaPago;
}
