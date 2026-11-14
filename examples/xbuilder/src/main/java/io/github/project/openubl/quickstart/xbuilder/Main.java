package io.github.project.openubl.quickstart.xbuilder;

import io.github.project.openubl.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.models.Cliente;
import io.github.project.openubl.xbuilder.models.Defaults;
import io.github.project.openubl.xbuilder.models.Detalle;
import io.github.project.openubl.xbuilder.models.Invoice;
import io.github.project.openubl.xbuilder.models.Proveedor;
import io.github.project.openubl.xbuilder.signature.CertificateDetails;
import io.github.project.openubl.xbuilder.signature.CertificateDetailsFactory;
import io.github.project.openubl.xbuilder.signature.XMLSigner;
import io.github.project.openubl.xbuilder.signature.XmlSignatureHelper;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

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
        // Tax rates default to the current SUNAT ones; only the issue date is set here
        Defaults defaults = Defaults.builder().fechaEmision(LocalDate.of(2019, 12, 24)).build();
        return new XBuilder().build(invoiceFactory(), defaults);
    }

    public static Document signXML(String xml) throws Exception {
        InputStream ksInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("LLAMA-PE-CERTIFICADO-DEMO-12345678912.pfx");
        CertificateDetails certificate = CertificateDetailsFactory.create(ksInputStream, "password");
        return XMLSigner.signXML(xml, "Project OpenUBL", certificate.getX509Certificate(), certificate.getPrivateKey());
    }

    public static Invoice invoiceFactory() {
        return Invoice.builder()
                .serieNumero("F001-1")
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build())
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad("6") // RUC (Catalog6)
                        .build())
                .detalles(List.of(
                        Detalle.builder()
                                .descripcion("Item1")
                                .cantidad(new BigDecimal("10"))
                                .precio(new BigDecimal("100"))
                                .unidadMedida("KGM")
                                .build(),
                        Detalle.builder()
                                .descripcion("Item2")
                                .cantidad(new BigDecimal("10"))
                                .precio(new BigDecimal("100"))
                                .unidadMedida("KGM")
                                .build()))
                .build();
    }
}
