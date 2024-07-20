package io.github.project.openubl.xbuilder.content.catalogs;

import java.math.BigDecimal;

public enum Catalog22 implements Catalog {
    VENTA_INTERNA("01", BigDecimal.valueOf(2)),
    ADQUISICION_DE_COMBUSTIBLE("02", BigDecimal.valueOf(1)),
    AGENTE_DE_PERCEPCION_CON_TASA_ESPECIAL("03", BigDecimal.valueOf(0.5));

    private final String code;
    private final BigDecimal percent;

    Catalog22(String code, BigDecimal percent) {
        this.code = code;
        this.percent = percent;
    }

    @Override
    public String getCode() {
        return code;
    }

    public BigDecimal getPercent() {
        return percent;
    }
}
