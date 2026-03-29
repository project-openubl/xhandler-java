package io.github.project.openubl.xbuilder.content.models.standard.guia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuiaItemAttribute {
    private String code;
    private String name;
    private String value;
}
