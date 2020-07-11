---
id: smart_sendbill
title: Smart send
---

This is the recommended way of sending XML files to SUNAT. You just define the file and then `XSender` defines to which URL and additional information.

Use for sending:

- Invoices (boleta/factura).
- Credit notes (notas de crédito).
- Debit notes (notas de débito).
- Despatch documents (guias de remisión).
- Perceptions (percepciones).
- Retentions (retenciones).
- Voided documents (bajas).
- Summary documents (resumen diario).

## Global configuration

You need to configure `SmartBillServiceConfig`. You need to do this only once during the whole lifecycle of you software.

```java
SmartBillServiceConfig.getInstance()
    .withInvoiceAndNoteDeliveryURL("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
    .withPerceptionAndRetentionDeliveryURL("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
    .withDespatchAdviceDeliveryURL("https://e-beta.sunat.gob.pe/ol-ti-itemision-guia-gem-beta/billService");
```

## Send files

Once `SmartBillServiceConfig` is configured you can star sending your files:

```java
byte[] xml;
String username;
String password;

// Send file
SmartBillServiceModel result = SmartBillServiceManager.send(xml, username, password);

// Read result values
XmlContentModel xmlData = result.getXmlContentModel();
BillServiceModel serverResponse = result.getBillServiceModel();
```
