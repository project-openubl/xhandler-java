package e2e.renderer.invoice;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog8;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class InvoiceIscTest extends AbstractTest {

    @Test
    public void testIsc_sistemaAlValor() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.17"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();

        assertInput(input, "isc_sistemaAlValor.xml");
    }

    @Test
    public void testIsc_aplicacionAlMontoFijo() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .iscTipo(Catalog8.APLICACION_AL_MONTO_FIJO.getCode())
                        .tasaIsc(new BigDecimal("0.20"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();

        assertInput(input, "isc_aplicacionAlMontoFijo.xml");
    }

    @Test
    public void testIsc_sistemaDePreciosDeVentalAlPublico() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .iscTipo(Catalog8.SISTEMA_DE_PRECIOS_DE_VENTA_AL_PUBLICO.getCode())
                        .tasaIsc(new BigDecimal("0.10"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();

        assertInput(input, "isc_sistemaDePreciosDeVentalAlPublico.xml");
    }

    @Test
    public void testIsc_precioConImpuestos() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("138.06"))
                        .precioConImpuestos(true)
                        .icbAplica(true)
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.17"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_PREMIO.getCode())
                        .precio(new BigDecimal("100"))
                        .precioConImpuestos(true)
                        .icbAplica(true)
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.17"))
                        .build()
                )
                .build();

        assertInput(input, "isc_precioConImpuestos.xml");
    }

    @Test
    public void testIsc_mixedTipoIgv() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.GRAVADO_RETIRO_POR_PREMIO.getCode())
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.10"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item3")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.EXONERADO_OPERACION_ONEROSA.getCode())
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.10"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item4")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.EXONERADO_TRANSFERENCIA_GRATUITA.getCode())
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.17"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item5")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.INAFECTO_OPERACION_ONEROSA.getCode())
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.10"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item6")
                        .cantidad(new BigDecimal("2"))
                        .precio(new BigDecimal("100"))
                        .igvTipo(Catalog7.INAFECTO_RETIRO_POR_BONIFICACION.getCode())
                        .iscTipo(Catalog8.SISTEMA_AL_VALOR.getCode())
                        .tasaIsc(new BigDecimal("0.10"))
                        .build()
                )
                .build();

        assertInput(input, "isc_mixedTipoIgv.xml");
    }
}
