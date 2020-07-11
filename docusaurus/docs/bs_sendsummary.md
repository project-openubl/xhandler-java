---
id: bs_sendsummary
title: BillService:sendSummary
---

Use for sending:

- Voided documents (Baja).
- Summary documents (Resumen diario).

## Send File

Define your URL you want yo send your file to, and then send your file:

```java
ServiceConfig config = new ServiceConfig.Builder()
    .url("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
    .username("12345678959MODDATOS")
    .password("MODDATOS")
    .build();

File file = new File("12345678959-RA-20180316-00001.xml");
BillServiceModel result = BillServiceManager.sendSummary(file, config);
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

byte[] file // Define tu array de bytes acá;
String fileName = "12345678959-RA-20180316-00001.xml";
BillServiceModel result = BillServiceManager.sendSummary(fileName, file, config);
```

> Remember that your filename must follow the pattern expected by the SOAP endpoint. E.g. for SUNAT it is expected to have the pattern `ruc-codigoComprobante-serie-numero.xml`
