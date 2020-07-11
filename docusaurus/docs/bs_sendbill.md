---
id: bs_sendbill
title: BillService:sendBill
---

Use for sending:

- Invoices (boleta/factura).
- Credit notes (notas de crédito).
- Debit notes (notas de débito).
- Despatch documents (guias de remisión).
- Perceptions (percepciones).
- Retentions (retenciones).

## Send File

Define your URL you want yo send your file to, and then send your file:

```java
ServiceConfig config = new ServiceConfig.Builder()
    .url("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
    .username("12345678959MODDATOS")
    .password("MODDATOS")
    .build();

File file = new File("12345678959-01-F001-00000001.xml");
BillServiceModel result = BillServiceManager.sendBill(file, config);
```

> Remember that your filename must follow the pattern expected by the SOAP endpoint. E.g. for SUNAT it is expected to have the pattern `ruc-codigoComprobante-serie-numero.xml`

## Send byte[]

Define your URL you want yo send your file to, and then send your file:

```java
ServiceConfig config = new ServiceConfig.Builder()
    .url("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
    .username("12345678959MODDATOS")
    .password("MODDATOS")
    .build();

byte[] file; // define your file here;
String fileName = "12345678959-01-F001-00000001.xml";
BillServiceModel result = BillServiceManager.sendBill(fileName, file, config);
```

> Remember that your filename must follow the pattern expected by the SOAP endpoint. E.g. for SUNAT it is expected to have the pattern `ruc-codigoComprobante-serie-numero.xml`
