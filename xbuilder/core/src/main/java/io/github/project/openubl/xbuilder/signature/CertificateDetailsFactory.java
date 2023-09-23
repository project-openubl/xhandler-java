/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xbuilder.signature;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CertificateDetailsFactory {

    public static CertificateDetails create(InputStream is, String jksPassword) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException, IOException, CertificateException {
        CertificateDetails certDetails = null;

        boolean isAliasWithPrivateKey = false;
        KeyStore keyStore = KeyStore.getInstance("JKS");

        // Provide location of Java Keystore and password for access
        keyStore.load(is, jksPassword.toCharArray());

        // iterate over all aliases
        Enumeration<String> es = keyStore.aliases();
        String alias = "";
        while (es.hasMoreElements()) {
            alias = (String) es.nextElement();
            // if alias refers to a private key break at that point
            // as we want to use that certificate
            if (isAliasWithPrivateKey = keyStore.isKeyEntry(alias)) {
                break;
            }
        }

        if (isAliasWithPrivateKey) {
            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(
                    alias,
                    new KeyStore.PasswordProtection(jksPassword.toCharArray())
            );
            PrivateKey myPrivateKey = pkEntry.getPrivateKey();

            // Load certificate chain
            Certificate[] chain = keyStore.getCertificateChain(alias);

            certDetails = new CertificateDetails();
            certDetails.setPrivateKey(myPrivateKey);
            certDetails.setX509Certificate((X509Certificate) chain[0]);
        }

        return certDetails;
    }
}
