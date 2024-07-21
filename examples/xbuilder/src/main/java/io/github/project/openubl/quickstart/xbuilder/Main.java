package io.github.project.openubl.quickstart.xbuilder;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import io.github.project.openubl.xbuilder.enricher.config.DateProvider;
import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.renderer.TemplateProducer;
import io.github.project.openubl.xbuilder.signature.CertificateDetails;
import io.github.project.openubl.xbuilder.signature.CertificateDetailsFactory;
import io.github.project.openubl.xbuilder.signature.XMLSigner;
import io.github.project.openubl.xbuilder.signature.XmlSignatureHelper;
import io.quarkus.qute.Template;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws Exception {
        // Create XML
        String xml = createUnsignedXML();

        System.out.println("Your XML is:");
        System.out.println(xml);

        // Sign XML
        Document signedXML = signXML(xml);

        byte[] bytesFromDocument = XmlSignatureHelper.getBytesFromDocument(signedXML);
        String signedXMLString = new String(bytesFromDocument, StandardCharsets.ISO_8859_1);

        System.out.println("\n Your signed XML is:");
        System.out.println(signedXMLString);
    }

    public static String createUnsignedXML() {
        // General config
        Defaults defaults = Defaults.builder()
                .icbTasa(new BigDecimal("0.2"))
                .igvTasa(new BigDecimal("0.18"))
                .build();

        DateProvider dateProvider = () -> LocalDate.of(2019, 12, 24);

        // Invoice generation
        Invoice invoice = invoiceFactory();

        // Enrich data
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(invoice);

        // Generate XML
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(invoice).render();

        return xml;
    }

    public static Document signXML(String xml) throws Exception {
        InputStream ksInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("LLAMA-PE-CERTIFICADO-DEMO-12345678912.pfx");
        CertificateDetails certificate = CertificateDetailsFactory.create(ksInputStream, "password");

        X509Certificate x509Certificate = certificate.getX509Certificate();
        PrivateKey privateKey = certificate.getPrivateKey();
        return XMLSigner.signXML(xml, "Project OpenUBL", x509Certificate, privateKey);
    }

    public static Invoice invoiceFactory() {
        return Invoice
                .builder()
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
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("100"))
                        .unidadMedida("KGM")
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item2")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("100"))
                        .unidadMedida("KGM")
                        .build()
                )
                .build();
    }

}
