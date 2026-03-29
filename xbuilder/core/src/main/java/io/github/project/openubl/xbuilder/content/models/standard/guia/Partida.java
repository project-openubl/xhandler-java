package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Punto de partida del traslado en la Guía de Remisión Electrónica.
 * <p>
 * Se mapea a {@code cac:Shipment/cac:Delivery/cac:Despatch/cac:DespatchAddress} en el XML UBL. El UBIGEO y la dirección
 * son obligatorios.
 *
 * @since 2.0
 * @see Envio#getPartida()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partida {

    /** Código UBIGEO INEI del punto de partida (6 dígitos). */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String ubigeo;

    /** Dirección completa del punto de partida. */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccion;

    /**
     * Código de establecimiento del punto de partida
     */
    @Schema(description = "Código de local anexo de partida")
    private String codigoLocal;

    /**
     * RUC asociado al punto de partida
     */
    @Schema(description = "RUC asociado al punto de partida")
    private String ruc;
}
