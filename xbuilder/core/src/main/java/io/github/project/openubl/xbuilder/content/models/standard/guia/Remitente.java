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
public class Remitente {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 11, maxLength = 11, pattern = "[0-9]+")
    private String ruc;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String razonSocial;
}
