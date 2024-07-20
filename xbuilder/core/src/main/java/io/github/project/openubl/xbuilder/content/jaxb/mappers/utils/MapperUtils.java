package io.github.project.openubl.xbuilder.content.jaxb.mappers.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class MapperUtils {

    public static BigDecimal mapPorcentaje(BigDecimal number) {
        return Optional.ofNullable(number)
                .map(bigDecimal -> bigDecimal.divide(new BigDecimal("100"), 10, RoundingMode.HALF_EVEN))
                .orElse(null);
    }

}
