package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para documentos adicionales relacionados al transporte.
 * Catálogo 61 de SUNAT.
 * Basado en el modelo AdditionalDoc de greenter.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoAdicional {

    /**
     * Código del tipo de documento (Catálogo 61)
     */
    @Schema(description = "Catalogo 61", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumento;

    /**
     * Descripción del tipo de documento
     */
    @Schema(description = "Descripción del tipo de documento")
    private String tipoDocumentoDescripcion;

    /**
     * Número del documento
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String numero;

    /**
     * RUC del emisor del documento
     */
    @Schema(description = "RUC del emisor del documento")
    private String rucEmisor;
}
