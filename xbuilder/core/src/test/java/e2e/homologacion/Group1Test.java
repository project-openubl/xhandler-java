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
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Group1Test extends AbstractTest {

    @Order(1)
    @Test
    public void factura1Con3Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF11")
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
                .build();

        assertInput(input, "factura1Con3Items.xml");
    }

    @Order(2)
    @Test
    public void factura2Con2Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF11")
                .numero(2)
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
                .build();

        assertInput(input, "factura2Con2Items.xml");
    }

    @Order(3)
    @Test
    public void factura3Con1Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF11")
                .numero(3)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();

        assertInput(input, "factura3Con1Items.xml");
    }

    @Order(4)
    @Test
    public void factura4Con5Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF11")
                .numero(4)
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

        assertInput(input, "factura4Con5Items.xml");
    }

    @Order(5)
    @Test
    public void factura5Con4Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF11")
                .numero(5)
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
                .build();

        assertInput(input, "factura5Con4Items.xml");
    }

    @Order(6)
    @Test
    public void notaDeCreditoDeFactura2() throws Exception {
        CreditNote input = CreditNote.builder()
                .serie("FF11")
                .numero(1)
                .comprobanteAfectadoSerieNumero("FF11-2")
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
                .build();

        assertInput(input, "notaDeCreditoDeFactura2.xml");
    }

    @Order(7)
    @Test
    public void notaDeCreditoDeFactura3() throws Exception {
        CreditNote input = CreditNote.builder()
                .serie("FF11")
                .numero(2)
                .comprobanteAfectadoSerieNumero("FF11-3")
                .sustentoDescripcion("Homologacion")
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();

        assertInput(input, "notaDeCreditoDeFactura3.xml");
    }

    @Order(8)
    @Test
    public void notaDeCreditoDeFactura4() throws Exception {
        CreditNote input = CreditNote.builder()
                .serie("FF11")
                .numero(3)
                .comprobanteAfectadoSerieNumero("FF11-4")
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

        assertInput(input, "notaDeCreditoDeFactura4.xml");
    }

    @Order(9)
    @Test
    public void notaDeDebitoDeFactura2() throws Exception {
        DebitNote input = DebitNote.builder()
                .serie("FF11")
                .numero(1)
                .comprobanteAfectadoSerieNumero("FF11-2")
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
                .build();

        assertInput(input, "notaDeDebitoDeFactura2.xml");
    }

    @Order(10)
    @Test
    public void notaDeDebitoDeFactura3() throws Exception {
        DebitNote input = DebitNote.builder()
                .serie("FF11")
                .numero(2)
                .comprobanteAfectadoSerieNumero("FF11-3")
                .sustentoDescripcion("Homologacion")
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();

        assertInput(input, "notaDeDebitoDeFactura3.xml");
    }

    @Order(11)
    @Test
    public void notaDeDebitoDeFactura4() throws Exception {
        DebitNote input = DebitNote.builder()
                .serie("FF11")
                .numero(3)
                .comprobanteAfectadoSerieNumero("FF11-4")
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

        assertInput(input, "notaDeDebitoDeFactura4.xml");
    }
}
