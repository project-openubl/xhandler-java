package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog18 implements Catalog {
    TRANSPORTE_PUBLICO("01"),
    TRANSPORTE_PRIVADO("02");

    private final String code;

    Catalog18(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
