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

public enum Catalog9 implements Catalog {
    ANULACION_DE_LA_OPERACION("01"),
    ANULACION_POR_ERROR_EN_EL_RUC("02"),
    CORRECCION_POR_ERROR_EN_LA_DESCRIPCION("03"),
    DESCUENTO_GLOBAL("04"),
    DESCUENTO_POR_ITEM("05"),
    DEVOLUCION_TOTAL("06"),
    DEVOLUCION_POR_ITEM("07"),
    BONIFICACION("08"),
    DISMINUCION_EN_EL_VALOR("09"),
    OTROS_CONCEPTOS("10");

    private final String code;

    Catalog9(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
