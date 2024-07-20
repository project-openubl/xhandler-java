package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog8 implements Catalog {
    SISTEMA_AL_VALOR("01"),
    APLICACION_AL_MONTO_FIJO("02"),
    SISTEMA_DE_PRECIOS_DE_VENTA_AL_PUBLICO("02");

    private final String code;

    Catalog8(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
