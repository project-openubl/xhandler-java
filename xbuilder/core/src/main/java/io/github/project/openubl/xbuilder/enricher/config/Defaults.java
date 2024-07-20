package io.github.project.openubl.xbuilder.enricher.config;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Defaults {
    private BigDecimal icbTasa = new BigDecimal("0.5");
    private BigDecimal igvTasa = new BigDecimal("0.18");
    private BigDecimal ivapTasa = new BigDecimal("0.04");
}
