package io.github.project.openubl.xbuilder.content.models.sunat.baja;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoidedDocumentsItem {
    /**
     * Serie del comprobante
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serie;

    /**
     * Número del comprobante
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "99999999")
    private Integer numero;

    @Schema(description = "Catalogo 01")
    private String tipoComprobante;

    @Schema(description = "Descripción de la razón de baja")
    private String descripcionSustento;
}
