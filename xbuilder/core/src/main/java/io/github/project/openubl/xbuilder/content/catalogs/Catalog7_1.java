package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog7_1 implements Catalog {
    GRAVADO("01"),
    EXONERADO("02"),
    INAFECTO("03"),
    EXPORTACION("04"),
    GRATUITA("05");

    private final String code;

    Catalog7_1(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
