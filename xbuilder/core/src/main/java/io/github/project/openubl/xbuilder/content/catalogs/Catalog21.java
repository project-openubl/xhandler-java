package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Catálogo N.° 21 - Tipo de documento relacionado a la Guía de Remisión
 * Electrónica.
 * <p>
 * Fuente: Anexo N.° 8 de la RS 000123-2022/SUNAT.
 * Se utiliza en {@code cac:AdditionalDocumentReference / cbc:DocumentTypeCode}.
 */
public enum Catalog21 implements Catalog {
    /** 01 - Numeración DAM (Declaración Aduanera de Mercancías) */
    NUMERACION_DAM("01"),

    /** 02 - Número de orden de entrega */
    NUMERO_DE_ORDEN_DE_ENTREGA("02"),

    /** 03 - Número SCOP */
    NUMERO_SCOP("03"),

    /** 04 - Número de manifiesto de carga */
    NUMERO_DE_MANIFIESTO_DE_CARGA("04"),

    /** 05 - Número de constancia de detracción */
    NUMERO_DE_CONSTANCIA_DE_DETRACCION("05"),

    /** 06 - Otros */
    OTROS("06"),

    /** 09 - Guía de remisión remitente */
    GUIA_REMISION_REMITENTE("09"),

    /** 12 - Declaración Simplificada (DS) */
    DECLARACION_SIMPLIFICADA("12"),

    /** 31 - Guía de remisión transportista */
    GUIA_REMISION_TRANSPORTISTA("31"),

    /**
     * 49 - Ticket de salida ENAPU.
     * <p>
     * Vigente condicionalmente: la derogación del ticket de salida ha sido
     * pospuesta al 01-jul-2026 por RS 000133-2025/SUNAT.
     */
    TICKET_SALIDA("49"),

    /** 50 - Código de autorización emitido por SUNAT */
    CODIGO_AUTORIZACION_SUNAT("50");

    private final String code;

    Catalog21(String code) {
        this.code = code;
    }

    public static Optional<Catalog21> valueOfCode(String code) {
        return Stream.of(Catalog21.values()).filter(p -> p.code.equals(code)).findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
