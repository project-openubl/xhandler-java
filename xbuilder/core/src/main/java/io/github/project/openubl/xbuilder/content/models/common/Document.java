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
package io.github.project.openubl.xbuilder.content.models.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class Document {

    /**
     * Moneda en la que se emite el comprobante
     */
    @Schema(minLength = 3, maxLength = 3)
    private String moneda;

    /**
     * Fecha de emisión del comprobante. Ejemplo 2022-12-25 (YYYY-MM-SS)
     */
    @Schema(description = "Format: \"YYYY-MM-SS\". Ejemplo: 2022-12-25", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaEmision;

    /**
     * Proveedor del bien o servicio
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Proveedor proveedor;

    /**
     * Persona que firma electrónicamente el comprobante. Si es NULL los datos del proveedor son usados.
     */
    @Schema(description = "Persona que firma el comprobante. Si NULL los datos del proveedor son usados.")
    private Firmante firmante;

}
