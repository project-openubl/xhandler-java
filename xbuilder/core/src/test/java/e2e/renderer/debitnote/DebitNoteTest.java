package e2e.renderer.debitnote;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class DebitNoteTest extends AbstractTest {

    @Test
    public void testInvoiceWithCustomUnidadMedida() throws Exception {
        // Given
        DebitNote input = DebitNote.builder()
                .serie("FD01")
                .numero(1)
                .comprobanteAfectadoSerieNumero("F001-1")
                .sustentoDescripcion("mi sustento")
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
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();

        assertInput(input, "MinData_RUC.xml");
    }
}
