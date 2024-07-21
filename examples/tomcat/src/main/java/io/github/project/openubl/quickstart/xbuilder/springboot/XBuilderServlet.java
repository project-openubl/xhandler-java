/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * <p>
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.eclipse.org/legal/epl-2.0/
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.quickstart.xbuilder.springboot;

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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.LocalDate;

@WebServlet("/api/create-xml/")
public class XBuilderServlet extends HttpServlet {

    Defaults defaults = Defaults.builder()
            .icbTasa(new BigDecimal("0.2"))
            .igvTasa(new BigDecimal("0.18"))
            .build();

    DateProvider dateProvider = LocalDate::now;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = req.getReader().readLine();

        try {
            Invoice invoice = createInvoice(clientName);

            ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
            enricher.enrich(invoice);

            Template template = TemplateProducer.getInstance().getInvoice();
            String xml = template.data(invoice).render();

            // Sign XML
            InputStream ksInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("LLAMA-PE-CERTIFICADO-DEMO-12345678912.pfx");

            CertificateDetails certificate = CertificateDetailsFactory.create(ksInputStream, "password");


            X509Certificate x509Certificate = certificate.getX509Certificate();
            PrivateKey privateKey = certificate.getPrivateKey();
            Document signedXML = XMLSigner.signXML(xml, "Project OpenUBL", x509Certificate, privateKey);

            //  Return
            byte[] bytesFromDocument = XmlSignatureHelper.getBytesFromDocument(signedXML);

            String xmlString = new String(bytesFromDocument, StandardCharsets.ISO_8859_1);

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
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre(clientName)
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
