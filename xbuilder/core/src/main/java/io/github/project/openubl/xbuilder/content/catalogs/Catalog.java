package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Catalog {
    Supplier<? extends RuntimeException> invalidCatalogValue = (Supplier<RuntimeException>) () ->
            new IllegalStateException("No se pudo convertir el valor del catálogo");

    /**
     * @param <T>      Class you want to search for
     * @param enumType class you want to search for
     * @param code     the code or Enum value
     * @return an instance of Catalog which is equal to ValueOf or contains the same code
     */
    static <T extends Catalog> Optional<T> valueOfCode(Class<T> enumType, String code) {
        return Stream
                .of(enumType.getEnumConstants())
                .filter(p -> p.toString().equalsIgnoreCase(code) || p.getCode().equals(code))
                .findFirst();
    }

    String getCode();
}
