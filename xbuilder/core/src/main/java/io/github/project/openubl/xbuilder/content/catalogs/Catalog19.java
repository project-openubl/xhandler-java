package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog19 implements Catalog {
    ADICIONAR("1"),
    MODIFICAR("2"),
    ANULADO("3");

    private final String code;

    Catalog19(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
