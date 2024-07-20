package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog12_Anticipo implements Catalog {
    FACTURA_EMITIDA_POR_ANTICIPOS("02"),
    BOLETA_DE_VENTA_EMITIDA_POR_ANTICIPOS("03");

    private final String code;

    Catalog12_Anticipo(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
