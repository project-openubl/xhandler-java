package io.github.project.openubl.xbuilder.enricher.kie.rules.utils;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Impuesto {
    private BigDecimal baseImponible;

    private BigDecimal importe;
    private BigDecimal importeIgv;
    private BigDecimal importeIsc;
    private BigDecimal importeIcb;
}
