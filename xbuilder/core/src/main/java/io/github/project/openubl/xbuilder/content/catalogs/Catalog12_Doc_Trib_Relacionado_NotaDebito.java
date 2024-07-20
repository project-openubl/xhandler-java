package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog12_Doc_Trib_Relacionado_NotaDebito implements Catalog {
    TICKET_DE_SALIDA("04"),
    CODIGO_SCOP("05"),
    OTROS("99");

    private final String code;

    Catalog12_Doc_Trib_Relacionado_NotaDebito(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
