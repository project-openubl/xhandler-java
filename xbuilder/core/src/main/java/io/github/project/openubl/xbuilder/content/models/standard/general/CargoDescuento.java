package io.github.project.openubl.xbuilder.content.models.standard.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CargoDescuento {

    private String serieNumero;
    private String tipo;
    private BigDecimal monto;
    private BigDecimal porcentaje;
}
