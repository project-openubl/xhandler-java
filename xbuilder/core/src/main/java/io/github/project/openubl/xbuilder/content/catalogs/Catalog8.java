package io.github.project.openubl.xbuilder.content.catalogs;

/**
 * Catálogo 08: Sistema de cálculo del ISC (Impuesto Selectivo al Consumo).
 * <p>
 * Fuente: SUNAT – Guía XML Factura 2.1, Catálogo 08. Usado en {@code cac:TaxSubtotal/cac:TaxCategory/cbc:TierRange}.
 * </p>
 * <p>
 * Nota: Los códigos "02" y "03" corresponden a sistemas distintos del ISC según la normativa. El código correcto para
 * "Sistema de precios de venta al público" es "03" (corregido de la versión anterior que usaba "02" erróneamente).
 * </p>
 */
public enum Catalog8 implements Catalog {
    /** Sistema al valor (porcentaje sobre valor de venta). */
    SISTEMA_AL_VALOR("01"),
    /** Aplicación al monto fijo (monto fijo por unidad). */
    APLICACION_AL_MONTO_FIJO("02"),
    /** Sistema de precios de venta al público (ISC sobre PVP). */
    SISTEMA_DE_PRECIOS_DE_VENTA_AL_PUBLICO("03");

    private final String code;

    Catalog8(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
