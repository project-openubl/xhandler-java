/*
 * Catálogo 63 - Puertos
 *
 * Fuente normativa: Anexo N.° 8, Catálogo N.° 63 de la RS 000123-2022/SUNAT.
 * Se utiliza como código de ubicación en FirstArrivalPortLocation con LocationTypeCode=1.
 *
 * Nota: Los puertos mencionados explícitamente en el RCP art. 21 numeral 3.2.9
 * son Callao, Paita, Salaverry, Chimbote, Pisco, Ilo, Matarani y Chancay
 * (este último agregado por RS 000240-2024/SUNAT).
 */
package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Catálogo N.° 63 - Puertos nacionales.
 * <p>
 * Utilizado en {@code cac:FirstArrivalPortLocation / cbc:ID} con
 * {@code cbc:LocationTypeCode = 1} para indicar puerto de embarque/desembarque.
 */
public enum Catalog63 implements Catalog {

    CALLAO("CALLAO", "Puerto del Callao"),
    PAITA("PAITA", "Puerto de Paita"),
    SALAVERRY("SALAVERRY", "Puerto de Salaverry"),
    CHIMBOTE("CHIMBOTE", "Puerto de Chimbote"),
    PISCO("PISCO", "Puerto de Pisco"),
    ILO("ILO", "Puerto de Ilo"),
    MATARANI("MATARANI", "Puerto de Matarani"),

    /**
     * Puerto de Chancay - agregado por RS 000240-2024/SUNAT para comercio exterior.
     * Vigente desde 14-nov-2024.
     */
    CHANCAY("CHANCAY", "Puerto de Chancay");

    private final String code;
    private final String description;

    Catalog63(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<Catalog63> valueOfCode(String code) {
        return Stream.of(Catalog63.values())
                .filter(p -> p.code.equalsIgnoreCase(code))
                .findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
