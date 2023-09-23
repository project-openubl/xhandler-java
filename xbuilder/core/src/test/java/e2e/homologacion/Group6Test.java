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
package e2e.homologacion;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53;
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.general.Percepcion;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Group6Test extends AbstractTest {

    @Order(1)
    @Test
    public void factura1Con5Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF40")
                .numero(1)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .build()
                )

                .percepcion(Percepcion.builder()
                        .tipo(Catalog53.PERCEPCION_VENTA_INTERNA.getCode())
                        .porcentaje(new BigDecimal("0.02"))
                        .build()
                )
                .build();

        assertInput(input, "factura1Con5Items.xml");
    }

    //

    @Order(2)
    @Test
    public void notaDeCreditoDeFactura1() throws Exception {
        CreditNote input = CreditNote.builder()
                .serie("FF40")
                .numero(1)
                .comprobanteAfectadoSerieNumero("FF40-1")
                .sustentoDescripcion("Homologacion")
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .build()
                )
                .build();

        assertInput(input, "notaDeCreditoDeFactura1.xml");
    }

    //

    @Order(3)
    @Test
    public void notaDeDebitoDeFactura1() throws Exception {
        DebitNote input = DebitNote.builder()
                .serie("FF40")
                .numero(1)
                .comprobanteAfectadoSerieNumero("FF40-1")
                .sustentoDescripcion("Homologacion")
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .build()
                )
                .build();

        assertInput(input, "notaDeDebitoDeFactura1.xml");
    }
}
