package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog21 implements Catalog {
    NUMERACION_DAM("01"),
    NUMERO_DE_ORDEN_DE_ENTREGA("02"),
    NUMERO_SCOP("03"),
    NUMERO_DE_MANIFIESTO_DE_CARGA("04"),
    NUMERO_DE_CONSTANCIA_DE_DETRACCION("05"),
    OTROS("06");

    private final String code;

    Catalog21(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
