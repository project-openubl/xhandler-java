package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog53_DescuentoGlobal implements Catalog {
    DESCUENTO_GLOBAL_AFECTA_BASE_IMPONIBLE_IGV_IVAP("02"),
    DESCUENTO_GLOBAL_NO_AFECTA_BASE_IMPONIBLE_IGV_IVAP("03");

    private final String code;

    Catalog53_DescuentoGlobal(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
