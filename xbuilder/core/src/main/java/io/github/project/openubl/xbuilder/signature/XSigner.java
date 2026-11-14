package io.github.project.openubl.xbuilder.signature;

import io.github.project.openubl.xhandler.core.Xhandler;
import io.github.project.openubl.xhandler.core.XhandlerException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * Signs UBL documents with the enveloped RSA-SHA256 XML signature SUNAT
 * requires. The signature is placed in the first empty
 * {@code ext:ExtensionContent} of the document, as rendered by
 * {@link io.github.project.openubl.xbuilder.XBuilder}.
 * <p>
 * Thread-safe and stateless.
 */
public class XSigner {

    /**
     * @param xml UTF-8 or ISO-8859-1 XML
     * @return the XML with its signature, in UTF-8
     * @throws XhandlerException when the key or the document are invalid
     */
    public byte[] sign(byte[] xml, KeyPair key) {
        Map<String, Object> input = Map.of(
                "xml", Base64.getEncoder().encodeToString(xml),
                "key", key.toJson()
        );
        return Base64.getDecoder().decode(Xhandler.invoke("sign", input).asText());
    }

    /**
     * @return the XML with its signature
     * @throws XhandlerException when the key or the document are invalid
     */
    public String sign(String xml, KeyPair key) {
        return new String(sign(xml.getBytes(StandardCharsets.UTF_8), key), StandardCharsets.UTF_8);
    }
}
