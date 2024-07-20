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
public class Destinatario {

    @Schema(description = "Catalogo 06", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumentoIdentidad;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroDocumentoIdentidad;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}
