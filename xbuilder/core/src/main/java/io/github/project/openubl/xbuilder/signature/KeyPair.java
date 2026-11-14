package io.github.project.openubl.xbuilder.signature;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Map;

/**
 * The private key and certificate that sign the documents. The key is checked
 * when signing.
 */
public final class KeyPair {

    private final Map<String, Object> key;

    private KeyPair(Map<String, Object> key) {
        this.key = key;
    }

    /**
     * @param privateKey  PEM private key, PKCS#8 or PKCS#1
     * @param certificate PEM X.509 certificate
     */
    public static KeyPair fromPem(String privateKey, String certificate) {
        return new KeyPair(Map.of("pem", Map.of("private_key", privateKey, "certificate", certificate)));
    }

    /**
     * @param der      PKCS#12 keystore (.pfx, .p12)
     * @param password password of the keystore
     */
    public static KeyPair fromPkcs12(byte[] der, String password) {
        return new KeyPair(Map.of("pkcs12", Map.of("der", Base64.getEncoder().encodeToString(der), "password", password)));
    }

    /**
     * @param keyStore a loaded keystore whose private key can be exported
     *                 (not a hardware one, e.g. PKCS#11)
     * @param alias    entry of the private key and its certificate
     * @param password password of the entry
     */
    public static KeyPair fromKeyStore(KeyStore keyStore, String alias, char[] password) throws GeneralSecurityException {
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);
        Certificate certificate = keyStore.getCertificate(alias);
        if (privateKey == null || certificate == null) {
            throw new GeneralSecurityException("No private key and certificate for alias " + alias);
        }
        return fromPem(pem("PRIVATE KEY", privateKey.getEncoded()), pem("CERTIFICATE", certificate.getEncoded()));
    }

    Map<String, Object> toJson() {
        return key;
    }

    private static String pem(String type, byte[] der) {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(der);
        return "-----BEGIN " + type + "-----\n" + base64 + "\n-----END " + type + "-----\n";
    }
}
