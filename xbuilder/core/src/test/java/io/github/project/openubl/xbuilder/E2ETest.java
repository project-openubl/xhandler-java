package io.github.project.openubl.xbuilder;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xbuilder.models.Invoice;
import io.github.project.openubl.xbuilder.signature.SigningKey;
import io.github.project.openubl.xbuilder.signature.XSigner;
import io.github.project.openubl.xhandler.core.Conformance;
import io.github.project.openubl.xsender.FileSender;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Creates, signs and sends an invoice to SUNAT beta. It needs the network, so
 * it only runs with XHANDLER_E2E=true.
 */
@EnabledIfEnvironmentVariable(named = "XHANDLER_E2E", matches = "true")
class E2ETest {

    @Test
    void createSignAndSendToSunatBeta() {
        Invoice invoice = XBuilderTest.invoice();
        // A new number each run: SUNAT beta remembers the documents
        invoice.setSerieNumero("F001-" + System.currentTimeMillis() / 1000 % 100_000_000);
        String xml = new XBuilder().build(invoice);

        byte[] signed = new XSigner().sign(xml.getBytes(StandardCharsets.UTF_8), testSigningKey());

        FileSender xsender = new FileSender(
                CompanyURLs.beta(),
                CompanyCredentials.builder().username("12345678959MODDATOS").password("MODDATOS").build()
        );
        SunatResponse response = xsender.sendFile(signed);

        assertEquals(Status.ACEPTADO, response.getStatus(), () -> response.toString());
        assertEquals(0, response.getMetadata().getResponseCode());
    }

    /** The test certificate of the corpus, the one SUNAT beta accepts for RUC 12345678912 */
    private static SigningKey testSigningKey() {
        JsonNode pem = Conformance.cases("sign").stream()
                .filter(c -> c.name().equals("pem/min_data_ruc"))
                .findFirst().orElseThrow()
                .input().at("/key/pem");
        return SigningKey.fromPem(pem.get("private_key").asText(), pem.get("certificate").asText());
    }
}
