package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Detracción asociada a una factura electrónica.
 * <p>
 * Obligatoria cuando {@code tipoOperacion} = "1001" (Catálogo 51). Se renderiza en el XML como:
 * <ul>
 * <li>{@code cac:PaymentMeans} con {@code PaymentMeansCode} = Catálogo 59</li>
 * <li>{@code cac:PaymentTerms} con monto, porcentaje y código de detracción (Catálogo 54)</li>
 * </ul>
 * <p>
 * <b>Regla SUNAT:</b> El monto de detracción se calcula como {@code porcentaje × importeConImpuestos} y se auto-calcula
 * por el enricher si no se especifica explícitamente.
 * </p>
 *
 * @see io.github.project.openubl.xbuilder.content.catalogs.Catalog54
 * @see io.github.project.openubl.xbuilder.content.catalogs.Catalog59
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
