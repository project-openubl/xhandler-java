package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

public enum Catalog1_Guia implements Catalog {
    GUIA_REMISION_REMITENTE("09"),
    GUIA_REMISION_TRANSPORTISTA("31");

    private final String code;

    Catalog1_Guia(String code) {
        this.code = code;
    }

    public static Optional<Catalog1_Guia> valueOfCode(String code) {
        return Stream.of(Catalog1_Guia.values()).filter(p -> p.code.equals(code)).findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
