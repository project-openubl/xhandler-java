package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Guia {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serieNumero;

    /**
     * Tipo de documento Guia.
     * <p>
     * Catalogo 01.
     * <p>
     * Valores válidos: "09", "31"
     */
    @Schema(description = "Catalogo 01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumento;
}
