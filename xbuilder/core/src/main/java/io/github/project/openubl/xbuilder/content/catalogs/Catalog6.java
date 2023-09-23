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

public enum Catalog6 implements Catalog {
    DOC_TRIB_NO_DOM_SIN_RUC("0"),
    DNI("1"),
    EXTRANJERIA("4"),
    RUC("6"),
    PASAPORTE("7"),
    DEC_DIPLOMATICA("A"),
    DOC_IDENT_PAIS_RESIDENCIA_NO_D("B"),
    TAX_IDENTIFICATION_NUMBER_TIN_DOC_TRIB_PP_NN("C"),
    IDENTIFICATION_NUMBER_IN_DOC_TRIB_PP_JJ("D"),
    TAM_TARJETA_ANDINA_DE_MIGRACION("E"),
    PERMISO_TEMPORAL_DE_PERMANENCIA_PTP("F"),
    SALVOCONDUCTO("G");

    private final String code;

    Catalog6(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
