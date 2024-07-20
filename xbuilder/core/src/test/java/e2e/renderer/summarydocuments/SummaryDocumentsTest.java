package e2e.renderer.summarydocuments;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog19;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Invoice;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.Comprobante;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteAfectado;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteImpuestos;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteValorVenta;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocuments;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocumentsItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class SummaryDocumentsTest extends AbstractTest {

    @Test
    public void testMultipleVoidedDocuments() throws Exception {
        // Given
        SummaryDocuments input = SummaryDocuments.builder()
                .numero(1)
                .fechaEmisionComprobantes(dateProvider.now().minusDays(2))
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .comprobante(SummaryDocumentsItem.builder()
                        .tipoOperacion(Catalog19.ADICIONAR.toString())
                        .comprobante(Comprobante.builder()
                                .tipoComprobante(Catalog1_Invoice.BOLETA.getCode())//
                                .serieNumero("B001-1")
                                .cliente(Cliente.builder()
                                        .nombre("Carlos Feria")
                                        .numeroDocumentoIdentidad("12345678")
                                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                        .build()
                                )
                                .impuestos(ComprobanteImpuestos.builder()
                                        .igv(new BigDecimal("18"))
                                        .icb(new BigDecimal(2))
                                        .build()
                                )
                                .valorVenta(ComprobanteValorVenta.builder()
                                        .importeTotal(new BigDecimal("120"))
                                        .gravado(new BigDecimal("120"))
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .comprobante(SummaryDocumentsItem.builder()
                        .tipoOperacion(Catalog19.ADICIONAR.toString())
                        .comprobante(Comprobante.builder()
                                .tipoComprobante(Catalog1.NOTA_CREDITO.getCode())
                                .serieNumero("BC02-2")
                                .comprobanteAfectado(ComprobanteAfectado.builder()
                                        .serieNumero("B002-2")
                                        .tipoComprobante(Catalog1.BOLETA.getCode()) //
                                        .build()
                                )
                                .cliente(Cliente.builder()
                                        .nombre("Carlos Feria")
                                        .numeroDocumentoIdentidad("12345678")
                                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())//
                                        .build()
                                )
                                .impuestos(ComprobanteImpuestos.builder()
                                        .igv(new BigDecimal("18"))
                                        .build()
                                )
                                .valorVenta(ComprobanteValorVenta.builder()
                                        .importeTotal(new BigDecimal("118"))
                                        .gravado(new BigDecimal("118"))
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();

        assertInput(input, "summaryDocuments.xml");
    }

    @Test
    public void testVoidedDocument_anularBoletaExonerada() throws Exception {
        // Given
        SummaryDocuments input = SummaryDocuments.builder()
                .numero(1)
                .fechaEmisionComprobantes(dateProvider.now().minusDays(2))
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .comprobante(SummaryDocumentsItem.builder()
                        .tipoOperacion(Catalog19.ANULADO.toString())
                        .comprobante(Comprobante.builder()
                                .tipoComprobante(Catalog1_Invoice.BOLETA.getCode())//
                                .serieNumero("B001-1")
                                .cliente(Cliente.builder()
                                        .nombre("Carlos Feria")
                                        .numeroDocumentoIdentidad("12345678")
                                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                        .build()
                                )
                                .impuestos(ComprobanteImpuestos.builder()
                                        .igv(new BigDecimal("0"))
                                        .build()
                                )
                                .valorVenta(ComprobanteValorVenta.builder()
                                        .importeTotal(new BigDecimal("100"))
                                        .gravado(new BigDecimal("0"))
                                        .exonerado(new BigDecimal("0"))
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();

        assertInput(input, "summaryDocuments_anularBoletaExonerada.xml");
    }
}
