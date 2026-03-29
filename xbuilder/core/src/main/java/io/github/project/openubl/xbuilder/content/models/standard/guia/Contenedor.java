package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para contenedor y precinto de transporte.
 * <p>
 * Según el Anexo N.° 14 UBL 2.1, cada contenedor se representa en
 * {@code cac:TransportHandlingUnit / cac:Package} con su número de contenedor
 * (cbc:ID) y opcionalmente su número de precinto (cbc:TraceID).
 * <p>
 * RS 000240-2024/SUNAT agrega campos adicionales para comercio exterior
 * (mercancía extranjera y zona primaria).
 *
 * @since 2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contenedor {

    /**
     * Número del contenedor.
     * <p>
     * Requerido cuando el motivo de traslado involucra comercio exterior
     * (importación, exportación, mercancía extranjera).
     * Para motivos domésticos es opcional.
     */
    @Schema(description = "Número del contenedor", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numero;

    /**
     * Número de precinto del contenedor.
     * <p>
     * Opcional. Se consigna en {@code cbc:TraceID}.
     */
    @Schema(description = "Número de precinto del contenedor")
    private String precinto;
}
