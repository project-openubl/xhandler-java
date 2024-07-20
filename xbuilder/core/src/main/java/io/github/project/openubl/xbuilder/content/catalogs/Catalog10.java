package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog10 implements Catalog {
    INTERES_POR_MORA("01"),
    AUMENTO_EN_EL_VALOR("02"),
    PENALIDAD_OTROS_CONCEPTOS("03");

    private final String code;

    Catalog10(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
