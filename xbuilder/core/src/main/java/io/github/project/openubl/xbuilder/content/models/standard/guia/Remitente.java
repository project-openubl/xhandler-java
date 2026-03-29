package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos del remitente de los bienes en la Guía de Remisión Electrónica.
 * <p>
 * En una GRE-Remitente (09), el remitente es quien envía los bienes (se mapea a {@code cac:DespatchSupplierParty}). En
 * una GRE-Transportista (31), este campo contiene los datos del transportista emitente (el mapeo se hace vía
 * {@link GRETransportista#toDespatchAdvice()}).
 *
 * @since 2.0
 * @see DespatchAdvice#getRemitente()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Remitente {

    /** RUC del remitente (11 dígitos, obligatorio). */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 11, maxLength = 11, pattern = "[0-9]+")
    private String ruc;

    /** Razón social del remitente. */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String razonSocial;

    /** Número de registro del Ministerio de Transportes y Comunicaciones (opcional). */
    @Schema(description = "Número de registro del Ministerio de Transportes y Comunicaciones")
    private String numeroRegistroMTC;
}
