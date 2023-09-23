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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;

public class XMLSigner {

    public static Document signXML(String text, String referenceID, X509Certificate certificate, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, XMLSignatureException, MarshalException, IOException, SAXException, ParserConfigurationException {
        Document document = XmlSignatureHelper.convertStringToXMLDocument(text);
        return signXML(document, referenceID, certificate, privateKey);
    }

    public static Document signXML(
            Document document,
            String referenceID,
            X509Certificate certificate,
            PrivateKey privateKey
    )
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, MarshalException, XMLSignatureException, ParserConfigurationException {
        addUBLExtensions(document);
        addUBLExtension(document);
        Node nodeExtensionContent = addExtensionContent(document);

        // Start the signing process
        DOMSignContext signContext = new DOMSignContext(privateKey, document.getDocumentElement());
        signContext.setDefaultNamespacePrefix("ds");
        signContext.setParent(nodeExtensionContent);

        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

        Reference reference = signatureFactory.newReference(
                "",
                signatureFactory.newDigestMethod(DigestMethod.SHA1, null),
                Collections.singletonList(
                        signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)
                ),
                null,
                null
        );

        SignedInfo signedInfo = signatureFactory.newSignedInfo(
                signatureFactory.newCanonicalizationMethod(
                        CanonicalizationMethod.INCLUSIVE,
                        (C14NMethodParameterSpec) null
                ),
                signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(reference)
        );

        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
        X509Data xdata = keyInfoFactory.newX509Data(Collections.singletonList(certificate));
        KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(xdata));

        XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo, null, referenceID, null);
        signature.sign(signContext);

        return document;
    }

    private static void addUBLExtensions(Document document) {
        NodeList nodeListUBLExtensions = document.getDocumentElement().getElementsByTagName("ext:UBLExtensions");
        Node nodeUBLExtensions = nodeListUBLExtensions.item(0);
        if (nodeUBLExtensions == null) {
            Element documentElement = document.getDocumentElement();
            nodeUBLExtensions = document.createElement("ext:UBLExtensions");
            documentElement.insertBefore(nodeUBLExtensions, documentElement.getFirstChild());
        }
    }

    private static void addUBLExtension(Document document) {
        NodeList nodeListUBLExtensions = document.getDocumentElement().getElementsByTagName("ext:UBLExtensions");
        NodeList nodeListUBLExtension = document.getDocumentElement().getElementsByTagName("ext:UBLExtension");
        Node nodeUBLExtension = nodeListUBLExtension.item(0);
        if (nodeUBLExtension == null) {
            nodeUBLExtension = document.createElement("ext:UBLExtension");
            nodeListUBLExtensions.item(0).appendChild(nodeUBLExtension);
        }
    }

    private static Node addExtensionContent(Document document) {
        NodeList nodeListUBLExtension = document.getDocumentElement().getElementsByTagName("ext:UBLExtension");
        NodeList nodeListExtensionContent = document.getDocumentElement().getElementsByTagName("ext:ExtensionContent");
        Node nodeExtensionContent = nodeListExtensionContent.item(0);
        if (nodeExtensionContent == null) {
            nodeExtensionContent = document.createElement("ext:ExtensionContent");
            nodeListUBLExtension.item(0).appendChild(nodeExtensionContent);
        }
        return nodeExtensionContent;
    }
}
