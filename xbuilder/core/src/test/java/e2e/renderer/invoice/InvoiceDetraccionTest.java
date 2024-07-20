package e2e.renderer.invoice;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog59;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.Detraccion;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class InvoiceDetraccionTest extends AbstractTest {

    @Test
    public void testFechaVencimiento() throws Exception {
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
                        .cantidad(new BigDecimal("4"))
                        .precio(new BigDecimal("200"))
                        .build()
                )
                .detraccion(Detraccion.builder()
                        .medioDePago(Catalog59.DEPOSITO_EN_CUENTA.getCode())
                        .cuentaBancaria("0004-3342343243")
                        .tipoBienDetraido("014")
                        .porcentaje(new BigDecimal("0.04"))
                        .monto(new BigDecimal(100))
                        .build()
                )
                .build();

        assertInput(input, "detraccion.xml");
    }
}
