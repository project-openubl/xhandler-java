package e2e.homologacion;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Group3Test extends AbstractTest {

    @Order(1)
    @Test
    public void factura1Con7Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF13")
                .numero(1)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_PREMIO.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_DONACION.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO.getCode())
                        .build()
                )

                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .igvTipo(Catalog7.EXONERADO_TRANSFERENCIA_GRATUITA.getCode())
                        .build()
                )

                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .igvTipo(Catalog7.INAFECTO_RETIRO_POR_BONIFICACION.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item6")
                        .cantidad(new BigDecimal("6"))
                        .precio(new BigDecimal("600"))
                        .igvTipo(Catalog7.INAFECTO_RETIRO.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item7")
                        .cantidad(new BigDecimal("7"))
                        .precio(new BigDecimal("700"))
                        .igvTipo(Catalog7.INAFECTO_RETIRO_POR_MUESTRAS_MEDICAS.getCode())
                        .build()
                )
                .build();

        assertInput(input, "factura1Con7Items.xml");
    }

    @Order(2)
    @Test
    public void factura2Con2Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF13")
                .numero(2)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_PREMIO.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_DONACION.getCode())
                        .build()
                )
                .build();

        assertInput(input, "factura2Con2Items.xml");
    }

    @Order(3)
    @Test
    public void factura3Con5Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF13")
                .numero(3)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_PREMIO.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_DONACION.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO.getCode())
                        .build()
                )

                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .igvTipo(Catalog7.EXONERADO_TRANSFERENCIA_GRATUITA.getCode())
                        .build()
                )

                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("5"))
                        .precio(new BigDecimal("500"))
                        .igvTipo(Catalog7.INAFECTO_RETIRO_POR_BONIFICACION.getCode())
                        .build()
                )
                .build();

        assertInput(input, "factura3Con5Items.xml");
    }

    @Order(4)
    @Test
    public void factura4Con4Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF13")
                .numero(4)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_PREMIO.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_DONACION.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO.getCode())
                        .build()
                )

                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("400"))
                        .igvTipo(Catalog7.EXONERADO_TRANSFERENCIA_GRATUITA.getCode())
                        .build()
                )
                .build();

        assertInput(input, "factura4Con4Items.xml");
    }

    @Order(5)
    @Test
    public void factura5Con3Items() throws Exception {
        Invoice input = Invoice.builder()
                .serie("FF13")
                .numero(5)
                .proveedor(HomologacionConstants.proveedor)
                .cliente(HomologacionConstants.cliente)
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_PREMIO.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("200"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_DONACION.getCode())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("3"))
                        .precio(new BigDecimal("300"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO.getCode())
                        .build()
                )
                .build();

        assertInput(input, "factura5Con3Items.xml");
    }

}
