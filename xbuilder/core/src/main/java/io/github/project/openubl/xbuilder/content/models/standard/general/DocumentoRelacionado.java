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
public class DocumentoRelacionado {

    @Schema(description = "Catalog 12", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumento;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serieNumero;
}
