package io.github.project.openubl.xbuilder.content.catalogs;

import java.math.BigDecimal;

public enum Catalog23 implements Catalog {
    TASA_TRES("01", BigDecimal.valueOf(3));

    private final String code;
    private final BigDecimal percent;

    Catalog23(String code, BigDecimal percent) {
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
