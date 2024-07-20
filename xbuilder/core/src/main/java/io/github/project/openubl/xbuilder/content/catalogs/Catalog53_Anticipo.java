package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog53_Anticipo implements Catalog {
    DESCUENTO_GLOBAL_POR_ANTICIPOS_GRAVADOS_AFECTA_BASE_IMPONIBLE_IGV_IVAP("04"),
    DESCUENTO_GLOBAL_POR_ANTICIPOS_EXONERADOS("05"),
    DESCUENTO_GLOBAL_POR_ANTICIPOS_INAFECTOS("06");

    private final String code;

    Catalog53_Anticipo(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
