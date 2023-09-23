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
package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog52 implements Catalog {
    MONTO_EN_LETRAS("1000", "MONTO EN LETRAS"),
    COMPROBANTE_DE_PERCEPCION("2000", "COMPROBANTE DE PERCEPCION"),
    VENTA_REALIZADA_POR_EMISOR_ITINERANTE("2005", "VENTA REALIZADA POR EMISOR ITINERANTE"),
    OPERACION_SUJETA_A_DETRACCION("2006", "OPERACION SUJETA A DETRACCION");

    private final String code;
    private final String label;

    Catalog52(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
