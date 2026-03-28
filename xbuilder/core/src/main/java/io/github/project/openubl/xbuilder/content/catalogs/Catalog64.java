/*
 * Catálogo 64 - Aeropuertos
 *
 * Fuente normativa: Anexo N.° 8, Catálogo N.° 64 de la RS 000123-2022/SUNAT.
 * Se utiliza como código de ubicación en FirstArrivalPortLocation con LocationTypeCode=2.
 */
package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Catálogo N.° 64 - Aeropuertos nacionales.
 * <p>
 * Utilizado en {@code cac:FirstArrivalPortLocation / cbc:ID} con
 * {@code cbc:LocationTypeCode = 2} para indicar aeropuerto de
 * embarque/desembarque.
 */
public enum Catalog64 implements Catalog {

    JORGE_CHAVEZ("LIM", "Aeropuerto Internacional Jorge Chávez"),
    RODRIGUEZ_BALLON("AQP", "Aeropuerto Alfredo Rodríguez Ballón"),
    ALEJANDRO_VELASCO("CUZ", "Aeropuerto Alejandro Velasco Astete"),
    CAP_FAP_CARLOS_MARTINEZ_DE_PINILLOS("TRU", "Aeropuerto Carlos Martínez de Pinillos"),
    CAP_FAP_JOSE_A_QUINONES("CIX", "Aeropuerto José A. Quiñones"),
    INCA_MANCO_CAPAC("JUL", "Aeropuerto Inca Manco Cápac"),
    PADRE_ALDAMIZ("PEM", "Aeropuerto Padre Aldamiz"),
    CORONEL_FAP_FRANCISCO_SECADA("IQT", "Aeropuerto Coronel FAP Francisco Secada"),
    CAP_FAP_DAVID_ABENSUR("PCL", "Aeropuerto David Abensur Rengifo"),
    MAYOR_GENERAL_FAP_ARMANDO_REVOREDO("CJA", "Aeropuerto Mayor General FAP Armando Revoredo");

    private final String code;
    private final String description;

    Catalog64(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<Catalog64> valueOfCode(String code) {
        return Stream.of(Catalog64.values())
                .filter(p -> p.code.equalsIgnoreCase(code))
                .findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
