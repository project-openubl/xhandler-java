package io.github.project.openubl.xsender.sunat.catalog;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class Catalog1Test {

    @Test
    public void givenValidCodeShouldReturnEnumValue() {
        Optional<Catalog1> result = Catalog1.valueOfCode("01");

        assertTrue(result.isPresent());
        assertEquals(Catalog1.FACTURA, result.get());
    }

    @Test
    public void givenInvalidCodeShouldReturnEmptyValue() {
        Optional<Catalog1> result = Catalog1.valueOfCode("010");

        assertFalse(result.isPresent());
    }

}
