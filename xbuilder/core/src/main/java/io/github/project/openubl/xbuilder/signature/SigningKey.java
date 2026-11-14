package io.github.project.openubl.xbuilder.signature;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * The private key and certificate that sign the documents. The key is checked
 * when signing.
 */
public final class SigningKey {

    private final Map<String, Object> key;

    private SigningKey(Map<String, Object> key) {
        this.key = key;
    }

    /**
     * @param privateKey  PEM private key, PKCS#8 or PKCS#1
     * @param certificate PEM X.509 certificate
     */
    public static SigningKey fromPem(String privateKey, String certificate) {
        Objects.requireNonNull(privateKey, "privateKey");
        Objects.requireNonNull(certificate, "certificate");
        return new SigningKey(Map.of("pem", Map.of("private_key", privateKey, "certificate", certificate)));
    }

    /**
     * @param der      PKCS#12 keystore (.pfx, .p12)
     * @param password password of the keystore
     */
    public static SigningKey fromPkcs12(byte[] der, String password) {
        Objects.requireNonNull(der, "der");
        Objects.requireNonNull(password, "password");
        return new SigningKey(Map.of("pkcs12", Map.of("der", Base64.getEncoder().encodeToString(der), "password", password)));
    }

    /**
     * @param keyStore a loaded keystore whose private key can be exported
     *                 (not a hardware one, e.g. PKCS#11)
     * @param alias    entry of the private key and its certificate
     * @param password password of the entry
     */
    public static SigningKey fromKeyStore(KeyStore keyStore, String alias, char[] password) throws GeneralSecurityException {
        Certificate certificate = keyStore.getCertificate(alias);
        if (!(keyStore.getKey(alias, password) instanceof PrivateKey privateKey) || certificate == null) {
            throw new GeneralSecurityException("No private key and certificate for alias " + alias);
        }
        if (privateKey.getEncoded() == null) {
            throw new GeneralSecurityException("The private key of alias " + alias + " can't be exported");
        }
        return fromPem(pem("PRIVATE KEY", privateKey.getEncoded()), pem("CERTIFICATE", certificate.getEncoded()));
    }

    Map<String, Object> toJson() {
        return key;
    }

    @Override
    public String toString() {
        return "SigningKey(<redacted>)";
    }

    private static String pem(String type, byte[] der) {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.US_ASCII)).encodeToString(der);
        return "-----BEGIN " + type + "-----\n" + base64 + "\n-----END " + type + "-----\n";
    }
}
