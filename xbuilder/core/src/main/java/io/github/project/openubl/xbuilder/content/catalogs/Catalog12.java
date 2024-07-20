package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog12 implements Catalog {
    FACTURA_EMITIDA_PARA_CORREGIR_ERROR_EN_EL_RUC("01"),
    FACTURA_EMITIDA_POR_ANTICIPOS("02"),
    BOLETA_DE_VENTA_EMITIDA_POR_ANTICIPOS("03"),
    TICKET_DE_SALIDA("04"),
    CODIGO_SCOP("05"),
    FACTURA_ELECTRONICA_REMITENTE("06"),
    GUIA_DE_REMISION_REMITENTE("07"),
    DECLARACION_DE_SALIDA_DEL_DEPOSITO_FRANCO("08"),
    DECLARACION_SIMPLIFICADA_DE_IMPORTACION("09"),
    LIQUIDACION_DE_COMPRA_EMITIDA_POR_ANTICIPOS("10"),
    OTROS("99");

    private final String code;

    Catalog12(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
