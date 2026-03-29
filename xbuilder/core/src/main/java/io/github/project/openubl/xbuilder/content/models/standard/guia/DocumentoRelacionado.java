package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Referencia a un documento relacionado con la Guía de Remisión Electrónica.
 * <p>
 * Se mapea a {@code cac:AdditionalDocumentReference} en el XML UBL. Permite vincular la GRE con facturas, guías
 * previas, u otros documentos tributarios (Catálogo 21).
 *
 * @since 2.0
 * @see DespatchAdvice#getDocumentoRelacionado()
 * @see DespatchAdvice#getDocumentosRelacionados()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoRelacionado {

    /** Tipo de documento relacionado (Catálogo 21). */
    @Schema(description = "Catalog 21", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumento;

    /** Serie-número del documento relacionado (ejemplo: "F001-456"). */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serieNumero;
}
