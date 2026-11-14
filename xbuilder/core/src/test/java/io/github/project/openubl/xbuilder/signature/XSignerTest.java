package io.github.project.openubl.xbuilder.signature;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.XBuilderTest;
import io.github.project.openubl.xhandler.core.Conformance;
import io.github.project.openubl.xhandler.core.XhandlerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XSignerTest {

    private final XSigner xsigner = new XSigner();

    static List<Conformance.Case> signCases() {
        // xml_not_base64 can't be expressed with bytes
        return Conformance.cases("sign").stream().filter(c -> !c.name().equals("errors/xml_not_base64")).toList();
    }

    static KeyPair keyPair(JsonNode key) {
        if (key.has("pem")) {
            return KeyPair.fromPem(key.at("/pem/private_key").asText(), key.at("/pem/certificate").asText());
        }
        return KeyPair.fromPkcs12(Base64.getDecoder().decode(key.at("/pkcs12/der").asText()), key.at("/pkcs12/password").asText());
    }

    @ParameterizedTest
    @MethodSource("signCases")
    void signCorpus(Conformance.Case c) {
        byte[] xml = Base64.getDecoder().decode(c.input().get("xml").asText());
        KeyPair key = keyPair(c.input().get("key"));

        if (c.expectsError()) {
            XhandlerException error = assertThrows(XhandlerException.class, () -> xsigner.sign(xml, key));
            assertEquals(c.expected().at("/error/kind").asText(), error.getKind().name().toLowerCase());
        } else {
            assertArrayEquals(Base64.getDecoder().decode(c.expected().get("ok").asText()), xsigner.sign(xml, key));
        }
    }

    /** A JDK keystore exports the same key pair as the PKCS#12 bytes */
    @Test
    void keyStoreSignsLikeItsPkcs12() throws Exception {
        JsonNode pkcs12 = Conformance.cases("sign").stream()
                .filter(c -> c.name().equals("pkcs12/llama"))
                .findFirst().orElseThrow()
                .input();
        byte[] der = Base64.getDecoder().decode(pkcs12.at("/key/pkcs12/der").asText());
        char[] password = pkcs12.at("/key/pkcs12/password").asText().toCharArray();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new ByteArrayInputStream(der), password);
        KeyPair fromKeyStore = KeyPair.fromKeyStore(keyStore, keyStore.aliases().nextElement(), password);

        String xml = new XBuilder().build(XBuilderTest.invoice());
        assertEquals(xsigner.sign(xml, KeyPair.fromPkcs12(der, new String(password))), xsigner.sign(xml, fromKeyStore));
        assertTrue(xsigner.sign(xml, fromKeyStore).contains("Id=\"PROJECT-OPENUBL-SIGN\""));
    }
}
