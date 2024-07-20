package e2e.homologacion;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Group5Test extends AbstractTest {

    @Order(1)
    @Test
    public void factura1Con5Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF30")
                .numero(1)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .tasaIsc(new BigDecimal("0.1"))
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
                .serie("FF30")
                .numero(1)
                .comprobanteAfectadoSerieNumero("FF30-1")
                .sustentoDescripcion("Homologacion")
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .build();

        assertInput(input, "notaDeCreditoDeFactura1.xml");
    }

    @Order(3)
    @Test
    public void notaDeDebitoDeFactura1() throws Exception {
        DebitNote input = DebitNote.builder()
                .serie("FF30")
                .numero(1)
                .comprobanteAfectadoSerieNumero("FF30-1")
                .sustentoDescripcion("Homologacion")
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .tasaIsc(new BigDecimal("0.1"))
                        .build()
                )
                .build();

        assertInput(input, "notaDeDebitoDeFactura2.xml");
    }

}
