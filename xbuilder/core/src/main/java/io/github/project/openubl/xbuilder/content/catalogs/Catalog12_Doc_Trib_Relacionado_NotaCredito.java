package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog12_Doc_Trib_Relacionado_NotaCredito implements Catalog {
    FACTURA_EMITIDA_PARA_CORREGIR_ERROR_EN_EL_RUC("01"),
    TICKET_DE_SALIDA("04"),
    CODIGO_SCOP("05"),
    OTROS("99");

    private final String code;

    Catalog12_Doc_Trib_Relacionado_NotaCredito(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
