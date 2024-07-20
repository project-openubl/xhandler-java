package e2e.renderer.invoice;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class InvoiceIssue30Test extends AbstractTest {

    @Test
    public void testInvoice_withPrecioUnitario() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Project OpenUBL S.A.C.").build())
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("6.68"))
                        .build()
                )
                .build();

        assertInput(input, "with-precioUnitario.xml");
    }

    @Test
    public void testInvoice_withPrecioConIgv() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Project OpenUBL S.A.C.").build())
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("7.88"))
                        .precioConImpuestos(true)
                        .build()
                )
                .build();

        assertInput(input, "with-precioUnitarioConImpuestos.xml");
    }

    @Test
    public void testInvoice_withPrecioUnitario_andICB() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Project OpenUBL S.A.C.").build())
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("6.68"))
                        .icbAplica(true)
                        .build()
                )
                .build();

        assertInput(input, "with-precioUnitario-ICB.xml");
    }

    @Test
    public void testInvoice_withPrecioConIgv_andICB() throws Exception {
        // Given
        Invoice input = Invoice.builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Project OpenUBL S.A.C.").build())
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("7.88"))
                        .precioConImpuestos(true)
                        .icbAplica(true)
                        .build()
                )
                .build();

        assertInput(input, "with-precioUnitario-conImpuestos-ICB.xml");
    }
}
