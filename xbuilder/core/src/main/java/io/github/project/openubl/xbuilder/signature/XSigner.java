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
     * @param xml UTF-8 or ISO-8859-1 XML, as its XML declaration says
     * @return the XML with its signature, in UTF-8 (callers that read the old
     * XMLSigner output as ISO-8859-1 must read it as UTF-8)
     * @throws XhandlerException when the key or the document are invalid
     */
    public byte[] sign(byte[] xml, SigningKey key) {
        Map<String, Object> input = Map.of(
                "xml", Base64.getEncoder().encodeToString(xml),
                "key", key.toJson()
        );
        return Base64.getDecoder().decode(Xhandler.invoke("sign", input).asText());
    }

    /**
     * @param xml the XML text; its XML declaration's encoding does not matter
     * @return the XML with its signature
     * @throws XhandlerException when the key or the document are invalid
     */
    public String sign(String xml, SigningKey key) {
        return new String(sign(xml.getBytes(StandardCharsets.UTF_8), key), StandardCharsets.UTF_8);
    }
}
