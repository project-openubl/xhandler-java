/*
 * Catálogo 20 - Motivo de traslado
 *
 * Fuente normativa: Anexo N.° 8 de la RS 000123-2022/SUNAT,
 *                   actualizado por RS 000240-2024/SUNAT.
 */
package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Catálogo N.° 20 - Motivo de traslado.
 * <p>
 * Vigente según RS 000123-2022/SUNAT y RS 000240-2024/SUNAT.
 * Aplicable tanto a GRE-Remitente (09) como GRE-Transportista (31).
 */
public enum Catalog20 implements Catalog {

    /** 01 - Venta */
    VENTA("01"),

    /** 02 - Compra */
    COMPRA("02"),

    /** 03 - Venta con entrega a terceros (consignación) */
    CONSIGNACION("03"),

    /** 04 - Traslado entre establecimientos de la misma empresa */
    TRASLADO_ENTRE_ESTABLECIMIENTOS("04"),

    /** 05 - Devolución */
    DEVOLUCION("05"),

    /** 06 - Traslado de bienes para transformación */
    TRASLADO_TRANSFORMACION("06"),

    /** 07 - Recojo de bienes transformados */
    RECOJO_BIENES_TRANSFORMADOS("07"),

    /** 08 - Importación */
    IMPORTACION("08"),

    /** 09 - Exportación */
    EXPORTACION("09"),

    /**
     * 10 - Importación: traslado de bienes con DAM/DS con levante.
     * <p>
     * Aplica cuando la mercancía tiene levante autorizado.
     * Incorporado por RS 000240-2024/SUNAT para trazabilidad de comercio exterior.
     * Vigente desde 14-nov-2024; uso como motivo mandatorio pospuesto al
     * 01-jul-2026.
     */
    IMPORTACION_CON_DAM("10"),

    /** 11 - Importación temporal */
    IMPORTACION_TEMPORAL("11"),

    /** 13 - Otros */
    OTROS("13"),

    /** 14 - Venta sujeta a confirmación del comprador */
    VENTA_SUJETA_A_CONFIRMACION("14"),

    /**
     * 15 - Traslado de bienes zona IVAP.
     * <p>
     * Aplica para traslados de bienes gravados con IVAP (arroz).
     */
    TRASLADO_ZONA_IVAP("15"),

    /** 16 - Exportación temporal (admisión temporal) */
    EXPORTACION_TEMPORAL("16"),

    /** 17 - Reexportación */
    REEXPORTACION("17"),

    /** 18 - Traslado emisor itinerante de comprobantes de pago */
    TRASLADO_EMISOR_ITINERANTE_CP("18"),

    /**
     * 19 - Traslado de mercancía extranjera (zona primaria a depósito temporal).
     * <p>
     * Uso obligatorio a partir del 01-jul-2026 para traslado de mercancía
     * extranjera sin destinación aduanera o sin levante, en reemplazo del ticket de
     * salida.
     * Vigencia de la derogación del ticket pospuesta por RS 000133-2025/SUNAT.
     */
    TRASLADO_MERCANCIA_EXTRANJERA("19");

    private final String code;

    Catalog20(String code) {
        this.code = code;
    }

    public static Optional<Catalog20> valueOfCode(String code) {
        return Stream.of(Catalog20.values()).filter(p -> p.code.equals(code)).findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
