package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog54 implements Catalog {
    AZUCAR("001"),
    ALCOHOL_ETILICO("003"),
    RECURSOS_HIDROBIOLOGICOS("004");

    private final String code;

    Catalog54(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
