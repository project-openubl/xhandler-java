package io.github.project.openubl.xbuilder.content.models.utils;

import lombok.Data;

import java.util.regex.Pattern;

@Data
public class UBLRegex {

    public static final Pattern FACTURA_SERIE_REGEX = Pattern.compile("^[F|f].*$");
    public static final Pattern BOLETA_SERIE_REGEX = Pattern.compile("^[B|b].*$");

    private UBLRegex() {
        // Just static methods
    }
}
