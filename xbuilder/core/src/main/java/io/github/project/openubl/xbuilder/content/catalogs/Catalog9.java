package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog9 implements Catalog {
    ANULACION_DE_LA_OPERACION("01"),
    ANULACION_POR_ERROR_EN_EL_RUC("02"),
    CORRECCION_POR_ERROR_EN_LA_DESCRIPCION("03"),
    DESCUENTO_GLOBAL("04"),
    DESCUENTO_POR_ITEM("05"),
    DEVOLUCION_TOTAL("06"),
    DEVOLUCION_POR_ITEM("07"),
    BONIFICACION("08"),
    DISMINUCION_EN_EL_VALOR("09"),
    OTROS_CONCEPTOS("10");

    private final String code;

    Catalog9(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
