package e2e.renderer.invoice;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.Descuento;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class InvoiceDescuentosTest extends AbstractTest {

    @Test
    public void descuentoGlobal() throws Exception {
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
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .descuento(Descuento.builder()
                        .monto(new BigDecimal("50"))
                        .build()
                )
                .build();

        assertInput(input, "descuentoGlobal.xml");
    }

    @Test
    public void descuentoGlobal_tipo02() throws Exception {
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
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .descuento(Descuento.builder()
                        .tipoDescuento(Catalog53.DESCUENTO_GLOBAL_AFECTA_BASE_IMPONIBLE_IGV_IVAP.getCode())
                        .monto(new BigDecimal("50"))
                        .build()
                )
                .build();

        assertInput(input, "descuentoGlobal_tipo02.xml");
    }

    @Test
    public void descuentoGlobal_tipo03() throws Exception {
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
                        .cantidad(new BigDecimal("1"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .descuento(Descuento.builder()
                        .tipoDescuento(Catalog53.DESCUENTO_GLOBAL_NO_AFECTA_BASE_IMPONIBLE_IGV_IVAP.getCode())
                        .monto(new BigDecimal("50"))
                        .build()
                )
                .build();

        assertInput(input, "descuentoGlobal_tipo03.xml");
    }

}
