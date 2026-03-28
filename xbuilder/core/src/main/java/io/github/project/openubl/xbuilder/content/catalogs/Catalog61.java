/*
 * Catálogo 61 - Tipo de documento adicional relacionado al transporte
 *
 * Fuente normativa: Anexo N.° 8, Catálogo N.° 61 de la RS 000123-2022/SUNAT.
 * Estos documentos se consignan en cac:AdditionalDocumentReference de la GRE
 * con un DocumentTypeCode basado en este catálogo.
 */
package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Catálogo N.° 61 - Tipo de documento adicional relacionado al transporte.
 * <p>
 * Aplicable a los campos {@code cac:AdditionalDocumentReference} de la GRE
 * correspondientes a documentos del ámbito de transporte y comercio exterior.
 */
public enum Catalog61 implements Catalog {

    /** 01 - Factura */
    FACTURA("01"),

    /** 02 - Boleta de venta */
    BOLETA_VENTA("02"),

    /** 03 - Liquidación de compra */
    LIQUIDACION_COMPRA("03"),

    /** 04 - Guía de remisión remitente */
    GUIA_REMISION_REMITENTE("04"),

    /** 05 - Guía de remisión transportista */
    GUIA_REMISION_TRANSPORTISTA("05"),

    /** 06 - Carta de porte aéreo */
    CARTA_PORTE_AEREO("06"),

    /** 07 - Póliza de adjudicación */
    POLIZA_ADJUDICACION("07"),

    /** 09 - Guía de remisión remitente complementaria */
    GUIA_REMISION_REMITENTE_COMP("09"),

    /** 10 - Guía de remisión transportista complementaria */
    GUIA_REMISION_TRANSPORTISTA_COMP("10"),

    /** 50 - DAM (Declaración Aduanera de Mercancías) */
    DAM("50"),

    /** 52 - Declaración Simplificada de Importación/Exportación */
    DECLARACION_SIMPLIFICADA("52");

    private final String code;

    Catalog61(String code) {
        this.code = code;
    }

    public static Optional<Catalog61> valueOfCode(String code) {
        return Stream.of(Catalog61.values()).filter(p -> p.code.equals(code)).findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
