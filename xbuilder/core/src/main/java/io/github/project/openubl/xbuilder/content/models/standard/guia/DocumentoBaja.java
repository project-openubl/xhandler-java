package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Referencia al documento dado de baja que justifica la emisión de una nueva Guía de Remisión Electrónica.
 * <p>
 * Se mapea a {@code cac:OrderReference} en el XML UBL. Aplica cuando la GRE actual reemplaza a una GRE previamente
 * anulada.
 *
 * @since 2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoBaja {

    /** Tipo de documento del comprobante dado de baja (Catálogo 01). */
    @Schema(description = "Catalog 01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumento;

    /** Serie-número del comprobante dado de baja (ejemplo: "T001-123"). */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serieNumero;
}
