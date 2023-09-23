/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xsender.sunat.catalog;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
