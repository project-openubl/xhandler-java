package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Detracción asociada a un Invoice
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detraccion {

    /**
     * Catalog59
     **/
    @Schema(description = "Catalogo 59", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medioDePago;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String cuentaBancaria;

    /**
     * Catalog54
     **/
    @Schema(description = "Catalog 54", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoBienDetraido;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", maximum = "1", exclusiveMinimum = true)
    private BigDecimal porcentaje;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", exclusiveMinimum = true)
    private BigDecimal monto;
}
