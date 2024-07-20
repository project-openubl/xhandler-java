package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoBaja {

    @Schema(description = "Catalog 01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumento;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serieNumero;
}
