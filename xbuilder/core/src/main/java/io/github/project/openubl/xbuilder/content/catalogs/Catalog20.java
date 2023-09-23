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

public enum Catalog20 implements Catalog {
    VENTA("01"),
    VENTA_SUJETA_A_CONFIRMACION_DEL_COMPRADOR("14"),
    COMPRA("02"),
    TRASLADO_ENTRE_ESTABLECIMIENTOS_DE_LA_MISMA_EMPRESA("04"),
    TRASLADO_EMISOR_ITINERANTE_CP("18"),
    IMPORTACION("08"),
    EXPORTACION("09"),
    TRASLADO_A_ZONA_PRIMARIA("19"),
    OTROS("13");

    private final String code;

    Catalog20(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
