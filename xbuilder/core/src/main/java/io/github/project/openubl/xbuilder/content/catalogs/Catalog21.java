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

public enum Catalog21 implements Catalog {
    NUMERACION_DAM("01"),
    NUMERO_DE_ORDEN_DE_ENTREGA("02"),
    NUMERO_SCOP("03"),
    NUMERO_DE_MANIFIESTO_DE_CARGA("04"),
    NUMERO_DE_CONSTANCIA_DE_DETRACCION("05"),
    OTROS("06");

    private final String code;

    Catalog21(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
