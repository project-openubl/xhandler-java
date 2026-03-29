package io.github.project.openubl.quickstart.xbuilder.springboot;

import io.github.project.openubl.xbuilder.content.catalogs.*;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.*;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.*;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.*;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.*;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import io.github.project.openubl.xbuilder.enricher.config.DateProvider;
import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.renderer.TemplateProducer;
import io.github.project.openubl.xbuilder.signature.CertificateDetails;
import io.github.project.openubl.xbuilder.signature.CertificateDetailsFactory;
import io.github.project.openubl.xbuilder.signature.XMLSigner;
import io.github.project.openubl.xbuilder.signature.XmlSignatureHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.LocalDate;

@RestController
public class XBuilderController {

        Defaults defaults = Defaults.builder()
                        .icbTasa(new BigDecimal("0.2"))
                        .igvTasa(new BigDecimal("0.18"))
                        .build();

        DateProvider dateProvider = LocalDate::now;

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/invoice", produces = "text/plain")
        public String createInvoiceXML(@RequestBody String clientName) throws Exception {
                Invoice invoice = createInvoice(clientName);

                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(invoice);
                String xml = TemplateProducer.getInstance().getInvoice().data(invoice).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/credit-note", produces = "text/plain")
        public String createCreditNoteXML(@RequestBody String clientName) throws Exception {
                CreditNote input = createCreditNote(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getCreditNote().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/debit-note", produces = "text/plain")
        public String createDebitNoteXML(@RequestBody String clientName) throws Exception {
                DebitNote input = createDebitNote(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getDebitNote().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/voided-documents", produces = "text/plain")
        public String createVoidedDocumentsXML(@RequestBody String clientName) throws Exception {
                VoidedDocuments input = createVoidedDocuments(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getVoidedDocument().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/summary-documents", produces = "text/plain")
        public String createSummaryDocumentsXML(@RequestBody String clientName) throws Exception {
                SummaryDocuments input = createSummaryDocuments(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getSummaryDocuments().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/perception", produces = "text/plain")
        public String createPerceptionXML(@RequestBody String clientName) throws Exception {
                Perception input = createPerception(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getPerception().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/retention", produces = "text/plain")
        public String createRetentionXML(@RequestBody String clientName) throws Exception {
                Retention input = createRetention(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getRetention().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/despatch-advice", produces = "text/plain")
        public String createDespatchAdviceXML(@RequestBody String clientName) throws Exception {
                DespatchAdvice input = createDespatchAdvice(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getDespatchAdvice().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/gre-remitente", produces = "text/plain")
        public String createGRERemitenteXML(@RequestBody String clientName) throws Exception {
                GRERemitente gre = createGRERemitente(clientName);
                DespatchAdvice input = gre.toDespatchAdvice();
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getDespatchAdvice().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/gre-transportista", produces = "text/plain")
        public String createGRETransportistaXML(@RequestBody String clientName) throws Exception {
                GRETransportista gre = createGRETransportista(clientName);
                DespatchAdvice input = gre.toDespatchAdvice();
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getDespatchAdvice().data(input).render();
                return signAndRender(xml);
        }

        @RequestMapping(method = RequestMethod.POST, value = "/api/create-xml/reversion", produces = "text/plain")
        public String createReversionXML(@RequestBody String clientName) throws Exception {
                Reversion input = createReversion(clientName);
                ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
                enricher.enrich(input);
                String xml = TemplateProducer.getInstance().getReversion().data(input).render();
                return signAndRender(xml);
        }

        private String signAndRender(String xml) throws Exception {
                // Sign XML
                InputStream ksInputStream = Thread.currentThread().getContextClassLoader()
                                .getResourceAsStream("LLAMA-PE-CERTIFICADO-DEMO-12345678912.pfx");
                CertificateDetails certificate = CertificateDetailsFactory.create(ksInputStream, "password");

                X509Certificate x509Certificate = certificate.getX509Certificate();
                PrivateKey privateKey = certificate.getPrivateKey();
                Document signedXML = XMLSigner.signXML(xml, "Project OpenUBL", x509Certificate, privateKey);

                // Return
                byte[] bytesFromDocument = XmlSignatureHelper.getBytesFromDocument(signedXML);
                return new String(bytesFromDocument, StandardCharsets.ISO_8859_1);
        }

        private Invoice createInvoice(String clientName) {
                return Invoice.builder()
                                .serie("F001")
                                .numero(1)
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .cliente(Cliente.builder()
                                                .nombre(clientName)
                                                .numeroDocumentoIdentidad("12121212121")
                                                .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                                                .build())
                                .detalle(DocumentoVentaDetalle.builder()
                                                .descripcion("Item1")
                                                .cantidad(new BigDecimal("10"))
                                                .precio(new BigDecimal("100"))
                                                .unidadMedida("KGM")
                                                .build())
                                .detalle(DocumentoVentaDetalle.builder()
                                                .descripcion("Item2")
                                                .cantidad(new BigDecimal("10"))
                                                .precio(new BigDecimal("100"))
                                                .unidadMedida("KGM")
                                                .build())
                                .build();
        }

        private CreditNote createCreditNote(String clientName) {
                return CreditNote.builder()
                                .serie("FC01")
                                .numero(1)
                                .comprobanteAfectadoSerieNumero("F001-1")
                                .sustentoDescripcion("mi sustento")
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .cliente(Cliente.builder()
                                                .nombre(clientName)
                                                .numeroDocumentoIdentidad("12121212121")
                                                .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                                                .build())
                                .detalle(DocumentoVentaDetalle.builder()
                                                .descripcion("Item1")
                                                .cantidad(new BigDecimal("10"))
                                                .precio(new BigDecimal("100"))
                                                .build())
                                .build();
        }

        private DebitNote createDebitNote(String clientName) {
                return DebitNote.builder()
                                .serie("FD01")
                                .numero(1)
                                .comprobanteAfectadoSerieNumero("F001-1")
                                .sustentoDescripcion("mi sustento")
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .cliente(Cliente.builder()
                                                .nombre(clientName)
                                                .numeroDocumentoIdentidad("12121212121")
                                                .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                                                .build())
                                .detalle(DocumentoVentaDetalle.builder()
                                                .descripcion("Item1")
                                                .cantidad(new BigDecimal("10"))
                                                .precio(new BigDecimal("100"))
                                                .build())
                                .build();
        }

        private VoidedDocuments createVoidedDocuments(String clientName) {
                return VoidedDocuments.builder()
                                .numero(1)
                                .fechaEmision(LocalDate.of(2022, 01, 31))
                                .fechaEmisionComprobantes(LocalDate.of(2022, 01, 29))
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .comprobante(VoidedDocumentsItem.builder()
                                                .serie("F001")
                                                .numero(1)
                                                .tipoComprobante(Catalog1_Invoice.FACTURA.getCode())
                                                .descripcionSustento("Mi sustento1")
                                                .build())
                                .comprobante(VoidedDocumentsItem.builder()
                                                .serie("F001")
                                                .numero(2)
                                                .tipoComprobante(Catalog1_Invoice.FACTURA.getCode())
                                                .descripcionSustento("Mi sustento2")
                                                .build())
                                .build();
        }

        private SummaryDocuments createSummaryDocuments(String clientName) {
                return SummaryDocuments.builder()
                                .numero(1)
                                .fechaEmisionComprobantes(dateProvider.now().minusDays(2))
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .comprobante(SummaryDocumentsItem.builder()
                                                .tipoOperacion(Catalog19.ADICIONAR.toString())
                                                .comprobante(Comprobante.builder()
                                                                .tipoComprobante(Catalog1_Invoice.BOLETA.getCode())//
                                                                .serieNumero("B001-1")
                                                                .cliente(Cliente.builder()
                                                                                .nombre(clientName)
                                                                                .numeroDocumentoIdentidad("12345678")
                                                                                .tipoDocumentoIdentidad(
                                                                                                Catalog6.DNI.getCode())
                                                                                .build())
                                                                .impuestos(ComprobanteImpuestos.builder()
                                                                                .igv(new BigDecimal("18"))
                                                                                .icb(new BigDecimal(2))
                                                                                .build())
                                                                .valorVenta(ComprobanteValorVenta.builder()
                                                                                .importeTotal(new BigDecimal("120"))
                                                                                .gravado(new BigDecimal("120"))
                                                                                .build())
                                                                .build())
                                                .build())
                                .build();
        }

        private Perception createPerception(String clientName) {
                return Perception.builder()
                                .serie("P001")
                                .numero(1)
                                .fechaEmision(LocalDate.of(2022, 01, 31))
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .cliente(Cliente.builder()
                                                .nombre(clientName)
                                                .numeroDocumentoIdentidad("12121212121")
                                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                                .build())
                                .importeTotalPercibido(new BigDecimal("10"))
                                .importeTotalCobrado(new BigDecimal("210"))
                                .tipoRegimen(Catalog22.VENTA_INTERNA.getCode())
                                .tipoRegimenPorcentaje(Catalog22.VENTA_INTERNA.getPercent()) //
                                .operacion(PercepcionRetencionOperacion.builder()
                                                .numeroOperacion(1)
                                                .fechaOperacion(LocalDate.of(2022, 01, 31))
                                                .importeOperacion(new BigDecimal("100"))
                                                .comprobante(io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.ComprobanteAfectado
                                                                .builder()
                                                                .tipoComprobante(Catalog1.FACTURA.getCode())
                                                                .serieNumero("F001-1")
                                                                .fechaEmision(LocalDate.of(2022, 01, 31))
                                                                .importeTotal(new BigDecimal("200"))
                                                                .moneda("PEN")
                                                                .build())
                                                .build())
                                .build();
        }

        private Retention createRetention(String clientName) {
                return Retention.builder()
                                .serie("R001")
                                .numero(1)
                                .fechaEmision(LocalDate.of(2022, 01, 31))
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .cliente(Cliente.builder()
                                                .nombre(clientName)
                                                .numeroDocumentoIdentidad("12121212121")
                                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                                .build())
                                .importeTotalRetenido(new BigDecimal("10"))
                                .importeTotalPagado(new BigDecimal("200"))
                                .tipoRegimen(Catalog23.TASA_TRES.getCode())
                                .tipoRegimenPorcentaje(Catalog23.TASA_TRES.getPercent()) //
                                .operacion(PercepcionRetencionOperacion.builder()
                                                .numeroOperacion(1)
                                                .fechaOperacion(LocalDate.of(2022, 01, 31))
                                                .importeOperacion(new BigDecimal("100"))
                                                .comprobante(io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.ComprobanteAfectado
                                                                .builder()
                                                                .tipoComprobante(Catalog1.FACTURA.getCode())
                                                                .serieNumero("F001-1")
                                                                .fechaEmision(LocalDate.of(2022, 01, 31))
                                                                .importeTotal(new BigDecimal("210"))
                                                                .moneda("PEN")
                                                                .build())
                                                .build())
                                .build();
        }

        private DespatchAdvice createDespatchAdvice(String clientName) {
                return DespatchAdvice.builder()
                                .serie("T001")
                                .numero(1)
                                .tipoComprobante(Catalog1.GUIA_REMISION_REMITENTE.getCode())
                                .remitente(Remitente.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .destinatario(Destinatario.builder()
                                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                                .numeroDocumentoIdentidad("12345678")
                                                .nombre(clientName)
                                                .build())
                                .envio(Envio.builder()
                                                .tipoTraslado(Catalog20.TRASLADO_EMISOR_ITINERANTE_CP.getCode())
                                                .pesoTotal(BigDecimal.ONE)
                                                .pesoTotalUnidadMedida("KG")
                                                .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                                                .fechaTraslado(dateProvider.now())
                                                .partida(Partida.builder()
                                                                .direccion("DireccionOrigen")
                                                                .ubigeo("010101")
                                                                .build())
                                                .destino(Destino.builder()
                                                                .direccion("DireccionDestino")
                                                                .ubigeo("020202")
                                                                .build())
                                                .build())
                                .detalle(DespatchAdviceItem.builder()
                                                .cantidad(new BigDecimal("0.5"))
                                                .unidadMedida("KG")
                                                .codigo("123456")
                                                .build())
                                .build();
        }

        private Reversion createReversion(String clientName) {
                return Reversion.builder()
                                .numero(1)
                                .fechaEmision(LocalDate.now())
                                .fechaEmisionComprobantes(LocalDate.now().minusDays(1))
                                .proveedor(Proveedor.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .comprobante(VoidedDocumentsItem.builder()
                                                .serie("P001")
                                                .numero(1)
                                                .tipoComprobante(Catalog1.PERCEPCION.getCode())
                                                .descripcionSustento("Anulacion de percepcion por error en emision")
                                                .build())
                                .comprobante(VoidedDocumentsItem.builder()
                                                .serie("R001")
                                                .numero(1)
                                                .tipoComprobante(Catalog1.RETENCION.getCode())
                                                .descripcionSustento("Anulacion de retencion por duplicado")
                                                .build())
                                .build();
        }

        /**
         * GRE-Remitente: transporte privado básico.
         * El remitente traslada bienes con su propio vehículo y conductor.
         */
        private GRERemitente createGRERemitente(String clientName) {
                return GRERemitente.builder()
                                .serie("T001")
                                .numero(1)
                                .remitente(Remitente.builder()
                                                .ruc("12345678912")
                                                .razonSocial("Softgreen S.A.C.")
                                                .build())
                                .destinatario(Destinatario.builder()
                                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                                .numeroDocumentoIdentidad("20200020002")
                                                .nombre(clientName)
                                                .build())
                                .envio(Envio.builder()
                                                .tipoTraslado(Catalog20.VENTA.getCode())
                                                .pesoTotal(new BigDecimal("50.000"))
                                                .pesoTotalUnidadMedida("KGM")
                                                .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                                                .fechaTraslado(dateProvider.now())
                                                .chofer(Driver.builder()
                                                                .tipo(TipoConductor.PRINCIPAL.getCode())
                                                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                                                .numeroDocumentoIdentidad("12345678")
                                                                .nombres("Carlos")
                                                                .apellidos("Ramirez")
                                                                .licencia("Q1234567")
                                                                .build())
                                                .vehiculo(Vehicle.builder()
                                                                .placa("ABC-123")
                                                                .build())
                                                .partida(Partida.builder()
                                                                .ubigeo("150101")
                                                                .direccion("Av. Industrial 456, Lima")
                                                                .build())
                                                .destino(Destino.builder()
                                                                .ubigeo("150102")
                                                                .direccion("Jr. Comercio 789, Rímac")
                                                                .build())
                                                .build())
                                .detalle(DespatchAdviceItem.builder()
                                                .cantidad(new BigDecimal("10.00"))
                                                .unidadMedida("NIU")
                                                .codigo("PROD-001")
                                                .descripcion("Cajas de producto terminado")
                                                .build())
                                .build();
        }

        /**
         * GRE-Transportista: el transportista emite la guía para trasladar bienes de un
         * cliente.
         * En esta casuística básica se consigna conductor y vehículo.
         */
        private GRETransportista createGRETransportista(String clientName) {
                return GRETransportista.builder()
                                .serie("V001")
                                .numero(1)
                                .transportistaEmisor(Transportista.builder()
                                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                                .numeroDocumentoIdentidad("12345678912")
                                                .nombre("Softgreen S.A.C.")
                                                .numeroRegistroMTC("MTC-001234")
                                                .build())
                                .remitente(Tercero.builder()
                                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                                .numeroDocumentoIdentidad("20100010001")
                                                .nombre("Empresa Remitente S.A.C.")
                                                .build())
                                .destinatario(Destinatario.builder()
                                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                                .numeroDocumentoIdentidad("20200020002")
                                                .nombre(clientName)
                                                .build())
                                .conductor(Driver.builder()
                                                .tipo(TipoConductor.PRINCIPAL.getCode())
                                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                                .numeroDocumentoIdentidad("44444444")
                                                .nombres("Miguel")
                                                .apellidos("Torres")
                                                .licencia("Q4444444")
                                                .build())
                                .vehiculo(Vehicle.builder()
                                                .placa("JKL-012")
                                                .build())
                                .envio(Envio.builder()
                                                .tipoTraslado(Catalog20.VENTA.getCode())
                                                .pesoTotal(new BigDecimal("300.000"))
                                                .pesoTotalUnidadMedida("KGM")
                                                .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                                                .fechaTraslado(dateProvider.now())
                                                .partida(Partida.builder()
                                                                .ubigeo("150101")
                                                                .direccion("Almacén Remitente, Lima")
                                                                .build())
                                                .destino(Destino.builder()
                                                                .ubigeo("040101")
                                                                .direccion("Sucursal Arequipa")
                                                                .build())
                                                .build())
                                .detalle(DespatchAdviceItem.builder()
                                                .cantidad(new BigDecimal("25.00"))
                                                .unidadMedida("NIU")
                                                .codigo("MER-001")
                                                .descripcion("Mercadería general")
                                                .build())
                                .build();
        }

}
