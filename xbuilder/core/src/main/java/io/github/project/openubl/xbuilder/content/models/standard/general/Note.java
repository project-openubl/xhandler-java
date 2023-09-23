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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Clase base para CreditNote y DebitNOte.
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class Note extends SalesDocument {

    /**
     * Tipo de nota.
     * <p>
     * Nota de Crédito: Catalogo 09.
     * <p>
     * Nota de Débito: Catalogo 10.
     */
    @Schema(description = "Si NotaCredito Catalog 09. Si NotaDebito Catalog 10")
    private String tipoNota;

    /**
     * Serie y número del comprobante al que le aplica la nota de crédito/débito.
     * Ejemplo: F001-1
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String comprobanteAfectadoSerieNumero;

    /**
     * Tipo de del probante referido en {@link Note#comprobanteAfectadoSerieNumero}.
     * <p>
     * Catalogo 01.
     */
    @Schema(description = "Catalog 01")
    private String comprobanteAfectadoTipo;

    /**
     * Texto sustentatorio para la emision de la nota
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String sustentoDescripcion;

    /**
     * Importe total de la nota
     */
    private TotalImporteNote totalImporte;
}
