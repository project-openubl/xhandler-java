package io.github.project.openubl.quickstart.xbuilder.springboot;

import io.github.project.openubl.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.models.Cliente;
import io.github.project.openubl.xbuilder.models.Detalle;
import io.github.project.openubl.xbuilder.models.Invoice;
import io.github.project.openubl.xbuilder.models.Proveedor;
import io.github.project.openubl.xbuilder.signature.KeyPair;
import io.github.project.openubl.xbuilder.signature.XSigner;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/api/create-xml/")
public class XBuilderServlet extends HttpServlet {

    private final XBuilder xbuilder = new XBuilder();
    private final XSigner xsigner = new XSigner();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String clientName = req.getReader().readLine();

        try {
            String xml = xbuilder.build(createInvoice(clientName));

            // Sign XML
            String xmlString;
            try (InputStream keyStore = getClass().getResourceAsStream("/LLAMA-PE-CERTIFICADO-DEMO-12345678912.pfx")) {
                xmlString = xsigner.sign(xml, KeyPair.fromPkcs12(keyStore.readAllBytes(), "password"));
            }

            // Response
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/plain");
            resp.getWriter().write(xmlString);
            resp.getWriter().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Invoice createInvoice(String clientName) {
        return Invoice.builder()
                .serieNumero("F001-1")
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build())
                .cliente(Cliente.builder()
                        .nombre(clientName)
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad("6") // RUC (Catalog6)
                        .build())
                .detalles(List.of(
                        Detalle.builder().descripcion("Item1").cantidad(new BigDecimal("10")).precio(new BigDecimal("100")).unidadMedida("KGM").build(),
                        Detalle.builder().descripcion("Item2").cantidad(new BigDecimal("10")).precio(new BigDecimal("100")).unidadMedida("KGM").build()))
                .build();
    }
}
