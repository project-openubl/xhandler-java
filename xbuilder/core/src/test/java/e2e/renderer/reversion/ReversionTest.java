package e2e.renderer.reversion;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.Reversion;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocumentsItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * Test for Reversion (Comunicacion de Baja de Retenciones/Percepciones).
 * Reversion uses prefix "RR-" instead of "RA-" for the document ID.
 */
public class ReversionTest extends AbstractTest {

    @Test
    public void testReversionWithPerceptions() throws Exception {
        // Given - Reverting perceptions (type 40)
        Reversion input = Reversion.builder()
                .numero(1)
                .fechaEmision(LocalDate.of(2022, 01, 31))
                .fechaEmisionComprobantes(LocalDate.of(2022, 01, 29))
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .comprobante(VoidedDocumentsItem.builder()
                        .serie("P001")
                        .numero(1)
                        .tipoComprobante(Catalog1.PERCEPCION.getCode())
                        .descripcionSustento("Anulacion de percepcion por error en emision")
                        .build()
                )
                .comprobante(VoidedDocumentsItem.builder()
                        .serie("P001")
                        .numero(2)
                        .tipoComprobante(Catalog1.PERCEPCION.getCode())
                        .descripcionSustento("Anulacion de percepcion por duplicado")
                        .build()
                )
                .build();

        assertInputReversion(input, "reversion.xml");
    }

    @Test
    public void testReversionWithRetentions() throws Exception {
        // Given - Reverting retentions (type 20)
        Reversion input = Reversion.builder()
                .numero(2)
                .fechaEmision(LocalDate.of(2022, 01, 31))
                .fechaEmisionComprobantes(LocalDate.of(2022, 01, 29))
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .comprobante(VoidedDocumentsItem.builder()
                        .serie("R001")
                        .numero(1)
                        .tipoComprobante(Catalog1.RETENCION.getCode())
                        .descripcionSustento("Anulacion de retencion por error en calculo")
                        .build()
                )
                .build();

        assertInputReversion(input, "reversion_retention.xml");
    }
}
