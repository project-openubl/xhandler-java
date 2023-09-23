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

import java.math.BigDecimal;

public enum Catalog22 implements Catalog {
    VENTA_INTERNA("01", BigDecimal.valueOf(2)),
    ADQUISICION_DE_COMBUSTIBLE("02", BigDecimal.valueOf(1)),
    AGENTE_DE_PERCEPCION_CON_TASA_ESPECIAL("03", BigDecimal.valueOf(0.5));

    private final String code;
    private final BigDecimal percent;

    Catalog22(String code, BigDecimal percent) {
        this.code = code;
        this.percent = percent;
    }

    @Override
    public String getCode() {
        return code;
    }

    public BigDecimal getPercent() {
        return percent;
    }
}
