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

import io.github.project.openubl.xbuilder.content.models.common.Direccion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Jacksonized
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Invoice extends SalesDocument {

    @Schema(description = "Ejemplo 2022-12-25", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaVencimiento;

    @Schema(description = "Catalogo 01")
    private String tipoComprobante;

    private String observaciones;

    /**
     * Catalog51
     */
    @Schema(description = "Catalogo 51")
    private String tipoOperacion;

    @Schema(description = "Forma de pago: al credito, o al contado")
    private FormaDePago formaDePago;

    @Schema(description = "Total importe del comprobante")
    private TotalImporteInvoice totalImporte;

    private Direccion direccionEntrega;
    private Detraccion detraccion;
    private Percepcion percepcion;

    /**
     * Anticipos asociados al comprobante
     */
    @Singular
    private List<Anticipo> anticipos;

    @Singular
    private List<Descuento> descuentos;
}
