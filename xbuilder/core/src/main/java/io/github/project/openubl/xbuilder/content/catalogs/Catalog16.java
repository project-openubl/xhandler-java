package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog16 implements Catalog {
    PRECIO_UNITARIO_INCLUYE_IGV("01"),
    VALOR_REFERENCIAL_UNITARIO_EN_OPERACIONES_NO_ONEROSAS("02");

    private final String code;

    Catalog16(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
