package io.github.project.openubl.quickstart.xbuilder.springboot;

import io.github.project.openubl.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.models.Cliente;
import io.github.project.openubl.xbuilder.models.CreditNote;
import io.github.project.openubl.xbuilder.models.DebitNote;
import io.github.project.openubl.xbuilder.models.DespatchAdvice;
import io.github.project.openubl.xbuilder.models.DespatchAdviceItem;
import io.github.project.openubl.xbuilder.models.Destinatario;
import io.github.project.openubl.xbuilder.models.Detalle;
import io.github.project.openubl.xbuilder.models.Document;
import io.github.project.openubl.xbuilder.models.Envio;
import io.github.project.openubl.xbuilder.models.Invoice;
import io.github.project.openubl.xbuilder.models.Partida;
import io.github.project.openubl.xbuilder.models.PercepcionRetencionComprobanteAfectado;
import io.github.project.openubl.xbuilder.models.PercepcionRetencionOperacion;
import io.github.project.openubl.xbuilder.models.Perception;
import io.github.project.openubl.xbuilder.models.Proveedor;
import io.github.project.openubl.xbuilder.models.Remitente;
import io.github.project.openubl.xbuilder.models.Retention;
import io.github.project.openubl.xbuilder.models.Reversion;
import io.github.project.openubl.xbuilder.models.SummaryDocuments;
import io.github.project.openubl.xbuilder.models.SummaryDocumentsCliente;
import io.github.project.openubl.xbuilder.models.SummaryDocumentsImpuestos;
import io.github.project.openubl.xbuilder.models.SummaryDocumentsItem;
import io.github.project.openubl.xbuilder.models.SummaryDocumentsItemComprobante;
import io.github.project.openubl.xbuilder.models.SummaryDocumentsValorVenta;
import io.github.project.openubl.xbuilder.models.VoidedDocuments;
import io.github.project.openubl.xbuilder.models.VoidedDocumentsItem;
import io.github.project.openubl.xbuilder.signature.SigningKey;
import io.github.project.openubl.xbuilder.signature.XSigner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Creates and signs a sample of every kind of document, e.g.
 * {@code POST /api/create-xml/invoice} with the client name as body.
 */
@RestController
public class XBuilderController {

    private final XBuilder xbuilder = new XBuilder();
    private final XSigner xsigner = new XSigner();

    @PostMapping(value = "/api/create-xml/{kind}", produces = "application/xml;charset=UTF-8")
    public String createXML(@PathVariable String kind, @RequestBody String clientName) throws Exception {
        Document document = switch (kind) {
            case "invoice" -> invoice(clientName);
            case "credit-note" -> creditNote(clientName);
            case "debit-note" -> debitNote(clientName);
            case "voided-documents" -> voidedDocuments();
            case "summary-documents" -> summaryDocuments();
            case "perception" -> perception(clientName);
            case "retention" -> retention(clientName);
            case "despatch-advice" -> despatchAdvice(clientName);
            case "reversion" -> reversion();
            default -> throw new ResponseStatusException(NOT_FOUND, "Unknown document: " + kind);
        };
        return sign(xbuilder.build(document));
    }

    private String sign(String xml) throws Exception {
        try (InputStream keyStore = getClass().getResourceAsStream("/LLAMA-PE-CERTIFICADO-DEMO-12345678912.pfx")) {
            return xsigner.sign(xml, SigningKey.fromPkcs12(keyStore.readAllBytes(), "password"));
        }
    }

    private static Proveedor proveedor() {
        return Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build();
    }

    private static Cliente cliente(String name) {
        return Cliente.builder()
                .nombre(name)
                .numeroDocumentoIdentidad("12121212121")
                .tipoDocumentoIdentidad("6") // RUC (Catalog6)
                .build();
    }

    private static List<Detalle> detalles() {
        return List.of(
                Detalle.builder().descripcion("Item1").cantidad(new BigDecimal("10")).precio(new BigDecimal("100")).unidadMedida("KGM").build(),
                Detalle.builder().descripcion("Item2").cantidad(new BigDecimal("10")).precio(new BigDecimal("100")).unidadMedida("KGM").build()
        );
    }

    private static Invoice invoice(String clientName) {
        return Invoice.builder()
                .serieNumero("F001-1")
                .proveedor(proveedor())
                .cliente(cliente(clientName))
                .detalles(detalles())
                .build();
    }

    private static CreditNote creditNote(String clientName) {
        return CreditNote.builder()
                .serieNumero("FC01-1")
                .comprobanteAfectadoSerieNumero("F001-1")
                .descripcionSustento("Anulación de operación")
                .proveedor(proveedor())
                .cliente(cliente(clientName))
                .detalles(detalles())
                .build();
    }

    private static DebitNote debitNote(String clientName) {
        return DebitNote.builder()
                .serieNumero("FD01-1")
                .comprobanteAfectadoSerieNumero("F001-1")
                .descripcionSustento("Intereses por mora")
                .proveedor(proveedor())
                .cliente(cliente(clientName))
                .detalles(detalles())
                .build();
    }

    private static VoidedDocuments voidedDocuments() {
        return VoidedDocuments.builder()
                .numero(1L)
                .fechaEmisionComprobantes(LocalDate.now().minusDays(1))
                .proveedor(proveedor())
                .comprobantes(List.of(VoidedDocumentsItem.builder()
                        .serie("F001")
                        .numero(1L)
                        .descripcionSustento("Error en el documento")
                        .build()))
                .build();
    }

    private static Reversion reversion() {
        return Reversion.builder()
                .numero(1L)
                .fechaEmisionComprobantes(LocalDate.now().minusDays(1))
                .proveedor(proveedor())
                .comprobantes(List.of(VoidedDocumentsItem.builder()
                        .serie("P001")
                        .numero(1L)
                        .descripcionSustento("Error en la percepción")
                        .build()))
                .build();
    }

    private static SummaryDocuments summaryDocuments() {
        return SummaryDocuments.builder()
                .numero(1L)
                .fechaEmisionComprobantes(LocalDate.now().minusDays(1))
                .proveedor(proveedor())
                .comprobantes(List.of(SummaryDocumentsItem.builder()
                        .tipoOperacion("1") // Adicionar (Catalog19)
                        .comprobante(SummaryDocumentsItemComprobante.builder()
                                .tipoComprobante("03") // Boleta (Catalog1)
                                .serieNumero("B001-1")
                                .cliente(SummaryDocumentsCliente.builder()
                                        .tipoDocumentoIdentidad("1") // DNI (Catalog6)
                                        .numeroDocumentoIdentidad("12345678")
                                        .build())
                                .valorVenta(SummaryDocumentsValorVenta.builder()
                                        .importeTotal(new BigDecimal("118"))
                                        .gravado(new BigDecimal("100"))
                                        .build())
                                .impuestos(SummaryDocumentsImpuestos.builder()
                                        .igv(new BigDecimal("18"))
                                        .igvTasa(new BigDecimal("18"))
                                        .build())
                                .build())
                        .build()))
                .build();
    }

    private static PercepcionRetencionOperacion operacion(BigDecimal importe) {
        return PercepcionRetencionOperacion.builder()
                .numeroOperacion(1L)
                .fechaOperacion(LocalDate.now())
                .importeOperacion(importe)
                .comprobanteAfectado(PercepcionRetencionComprobanteAfectado.builder()
                        .tipoComprobante("01") // Factura (Catalog1)
                        .serieNumero("F001-1")
                        .fechaEmision(LocalDate.now().minusDays(10))
                        .importeTotal(importe)
                        .moneda("PEN")
                        .build())
                .build();
    }

    private static Perception perception(String clientName) {
        return Perception.builder()
                .serie("P001")
                .numero(1L)
                .proveedor(proveedor())
                .cliente(cliente(clientName))
                .tipoRegimen("01")
                .tipoRegimenPorcentaje(new BigDecimal("2"))
                .importeTotalPercibido(new BigDecimal("4"))
                .importeTotalCobrado(new BigDecimal("204"))
                .operacion(operacion(new BigDecimal("200")))
                .build();
    }

    private static Retention retention(String clientName) {
        return Retention.builder()
                .serie("R001")
                .numero(1L)
                .proveedor(proveedor())
                .cliente(cliente(clientName))
                .tipoRegimen("01")
                .tipoRegimenPorcentaje(new BigDecimal("3"))
                .importeTotalRetenido(new BigDecimal("30"))
                .importeTotalPagado(new BigDecimal("970"))
                .operacion(operacion(new BigDecimal("1000")))
                .build();
    }

    private static DespatchAdvice despatchAdvice(String clientName) {
        return DespatchAdvice.builder()
                .serieNumero("T001-1")
                .remitente(Remitente.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad("1") // DNI (Catalog6)
                        .numeroDocumentoIdentidad("12345678")
                        .nombre(clientName)
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado("01") // Venta (Catalog20)
                        .pesoTotal(new BigDecimal("25.5"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado("02") // Transporte privado (Catalog18)
                        .fechaTraslado(LocalDate.now())
                        .partida(Partida.builder().ubigeo("150101").direccion("Av. Los Olivos 123").build())
                        .destino(Partida.builder().ubigeo("040101").direccion("Jr. Arequipa 456").build())
                        .build())
                .detalles(List.of(DespatchAdviceItem.builder()
                        .unidadMedida("NIU")
                        .cantidad(new BigDecimal("10"))
                        .codigo("PROD001")
                        .build()))
                .build();
    }
}
