package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Anticipo realizado.
 * <p>
 * Un anticipo se realiza cuando el cliente a entregado al proveedor un pago por anticipado a la prestación del servicio o la entrega.
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anticipo {

    /**
     * Tipo de anticipo realizado.
     * <p>
     * Catalogo 53.
     * <p>
     * Valores válidos: "04", "05", "06"
     */
    @Schema(example = "04", description = "Catalog 53", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"04", "05", "06"})
    private String tipo;

    /**
     * Serie y número de comprobante del anticipo, por ejemplo "F123-4"
     */
    @Schema(example = "F123-4", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comprobanteSerieNumero;

    /**
     * Catalog 12
     * Código de tipo de documento del {@link #comprobanteSerieNumero}.
     */
    @Schema(description = "Catalog 12. Tipo de documento del comprobante de anticipo")
    private String comprobanteTipo;

    /**
     * Monto prepagado o anticipado
     */
    @Schema(example = "10", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", exclusiveMinimum = true)
    private BigDecimal monto;
}
