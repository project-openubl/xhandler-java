package io.github.project.openubl.xbuilder.content.models.standard.guia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DespatchAdviceItem {
    private String unidadMedida;
    private BigDecimal cantidad;

    private String descripcion;
    private String codigo;
    private String codigoSunat;

    @Singular
    private List<GuiaItemAttribute> atributos;
}
