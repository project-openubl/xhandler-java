package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog1_Invoice implements Catalog {
    FACTURA("01"),
    BOLETA("03");

    private final String code;

    Catalog1_Invoice(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
