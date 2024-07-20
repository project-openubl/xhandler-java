package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog51 implements Catalog {
    VENTA_INTERNA("0101"),
    OPERACION_SUJETA_A_DETRACCION("1001"),
    OPERACION_SUJETA_A_PERCEPCION("2001");

    private final String code;

    Catalog51(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
