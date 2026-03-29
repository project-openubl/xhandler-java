package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Punto de destino/llegada del traslado en la Guía de Remisión Electrónica.
 * <p>
 * Se mapea a {@code cac:Shipment/cac:Delivery/cac:DeliveryAddress} en el XML UBL. El UBIGEO y la dirección son
 * obligatorios.
 *
 * @since 2.0
 * @see Envio#getDestino()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Destino {

    /** Código UBIGEO INEI del punto de destino (6 dígitos). */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String ubigeo;

    /** Dirección completa del punto de destino. */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccion;

    /**
     * Código de establecimiento del punto de llegada
     */
    @Schema(description = "Código de local anexo de llegada")
    private String codigoLocal;

    /**
     * RUC asociado al punto de llegada
     */
    @Schema(description = "RUC asociado al punto de llegada")
    private String ruc;
}
