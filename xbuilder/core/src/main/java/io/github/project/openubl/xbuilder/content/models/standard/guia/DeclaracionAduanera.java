package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para referencia a Declaración Aduanera de Mercancías (DAM)
 * o Declaración Simplificada (DS).
 * <p>
 * Según RS 000240-2024/SUNAT, cuando el motivo de traslado es Importación (10),
 * Exportación (09), o Traslado de Mercancía Extranjera (19), se debe consignar
 * la información de la DAM o DS.
 * <p>
 * Se mapea a un {@code cac:AdditionalDocumentReference} con el código de
 * catálogo 61
 * correspondiente (50=DAM, 52=DS).
 *
 * @since 2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeclaracionAduanera {

    /**
     * Tipo de declaración: "DAM" o "DS".
     * Se usa para determinar el código del Catálogo 61:
     * - DAM = "50"
     * - DS = "52"
     */
    @Schema(description = "Tipo: DAM o DS", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipo;

    /**
     * Número de la declaración aduanera.
     * Formato típico: 118-2024-10-XXXXXX
     */
    @Schema(description = "Número de la DAM/DS", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numero;

    /**
     * RUC de la aduana o agente que emitió la declaración (opcional).
     */
    @Schema(description = "RUC del emisor de la declaración")
    private String rucEmisor;
}
