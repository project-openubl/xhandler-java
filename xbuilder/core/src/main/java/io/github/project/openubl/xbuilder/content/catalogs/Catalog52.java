package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog52 implements Catalog {
    MONTO_EN_LETRAS("1000", "MONTO EN LETRAS"),
    COMPROBANTE_DE_PERCEPCION("2000", "COMPROBANTE DE PERCEPCION"),
    VENTA_REALIZADA_POR_EMISOR_ITINERANTE("2005", "VENTA REALIZADA POR EMISOR ITINERANTE"),
    OPERACION_SUJETA_A_DETRACCION("2006", "OPERACION SUJETA A DETRACCION");

    private final String code;
    private final String label;

    Catalog52(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
