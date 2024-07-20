package io.github.project.openubl.xbuilder.content.models.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoCambio {

    private LocalDate fecha;
    private BigDecimal valor;
}
