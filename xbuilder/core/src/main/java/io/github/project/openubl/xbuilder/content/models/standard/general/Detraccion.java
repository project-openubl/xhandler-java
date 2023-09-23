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
package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Detracción asociada a un Invoice
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detraccion {

    /**
     * Catalog59
     **/
    @Schema(description = "Catalogo 59", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medioDePago;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String cuentaBancaria;

    /**
     * Catalog54
     **/
    @Schema(description = "Catalog 54", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoBienDetraido;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", maximum = "1", exclusiveMinimum = true)
    private BigDecimal porcentaje;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", exclusiveMinimum = true)
    private BigDecimal monto;
}
