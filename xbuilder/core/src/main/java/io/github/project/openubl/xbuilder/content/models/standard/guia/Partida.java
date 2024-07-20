package io.github.project.openubl.xbuilder.content.models.standard.guia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partida {
    private String ubigeo;
    private String direccion;
}
