package io.github.project.openubl.xbuilder.content.catalogs;

public enum CatalogContadoCredito implements Catalog {
    CONTADO("Contado"),
    CREDITO("Credito");

    private final String code;

    CatalogContadoCredito(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
