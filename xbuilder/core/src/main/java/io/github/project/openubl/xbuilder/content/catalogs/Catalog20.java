package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog20 implements Catalog {
    VENTA("01"),
    VENTA_SUJETA_A_CONFIRMACION_DEL_COMPRADOR("14"),
    COMPRA("02"),
    TRASLADO_ENTRE_ESTABLECIMIENTOS_DE_LA_MISMA_EMPRESA("04"),
    TRASLADO_EMISOR_ITINERANTE_CP("18"),
    IMPORTACION("08"),
    EXPORTACION("09"),
    TRASLADO_A_ZONA_PRIMARIA("19"),
    OTROS("13");

    private final String code;

    Catalog20(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
