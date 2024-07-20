package io.github.project.openubl.xbuilder.content.models.sunat.resumen;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDocumentsItem {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Catalogo Catalog19")
    private String tipoOperacion;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Comprobante comprobante;
}
