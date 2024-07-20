package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog12_Doc_Trib_Relacionado_BoletaFactura implements Catalog {
    TICKET_DE_SALIDA("04"),
    CODIGO_SCOP("05"),
    FACTURA_ELECTRONICA_REMITENTE("06"),
    GUIA_DE_REMISION_REMITENTE("07"),
    DECLARACION_DE_SALIDA_DEL_DEPOSITO_FRANCO("08"),
    DECLARACION_SIMPLIFICADA_DE_IMPORTACION("09"),
    OTROS("99");

    private final String code;

    Catalog12_Doc_Trib_Relacionado_BoletaFactura(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
