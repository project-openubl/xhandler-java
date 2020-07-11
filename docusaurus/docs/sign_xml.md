---
id: sign_xml
title: Sign XML
---

For signing a XML you need to load your `X509Certificate` and `PrivateKey` using the method of your preference.

```java
String xml; // Use docs for create an XML
String signID = "mySignID"; // Your Signature ID

// Get your certificate using the method of your preference
X509Certificate certificate;
PrivateKey privateKey;

Document signedXML = XMLSigner.signXML(xml, signID, certificate, privateKey);
```
