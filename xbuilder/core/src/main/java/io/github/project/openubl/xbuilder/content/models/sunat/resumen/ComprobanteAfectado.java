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
public class ComprobanteAfectado {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Catalogo 01")
    private String tipoComprobante;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Comprobante afectado")
    private String serieNumero;
}
