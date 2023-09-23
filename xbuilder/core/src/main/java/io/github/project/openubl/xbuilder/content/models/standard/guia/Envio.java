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
package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Schema(description = "Catalog 20", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoTraslado;
    private String motivoTraslado;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal pesoTotal;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String pesoTotalUnidadMedida;

    private Integer numeroDeBultos;
    private boolean transbordoProgramado;

    @Schema(description = "Catalog 18", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoModalidadTraslado;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaTraslado;

    private String numeroDeContenedor;
    private String codigoDePuerto;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Transportista transportista;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Partida partida;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Destino destino;
}
