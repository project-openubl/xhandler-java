[![Build Status](https://travis-ci.org/carlosthe19916/sunat-web-services.svg?branch=master)](https://travis-ci.org/carlosthe19916/sunat-web-services)
[![Coverage Status](https://coveralls.io/repos/github/carlosthe19916/sunat-web-services/badge.svg?branch=master)](https://coveralls.io/github/carlosthe19916/sunat-web-services?branch=master)
[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=sunat-web-services&metric=alert_status)](https://sonarcloud.io/dashboard?id=sunat-web-services)

# sunat-web-services
Libreria que contiene los web endpoints para conectarse con la SUNAT.

Para incluirla en tu projecto puedes usar maven:

```
<dependency>
    <groupId>io.github.carlosthe19916</groupId>
    <artifactId>sunat-web-services</artifactId>
    <version>latestVersion</version>
</dependency>
```

## SUNAT Web Services 

BETA | URL
--- | ---
**Factura** | https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService
**Guía remisión** | https://e-beta.sunat.gob.pe/ol-ti-itemision-guia-gem-beta/billService
**Retenciones** | https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService

PRODUCCIÓN | URL
--- | ---
**Factura** | https://e-factura.sunat.gob.pe/ol-ti-itcpfegem/billService
**Guía remisión** | https://e-guiaremision.sunat.gob.pe/ol-ti-itemision-guia-gem/billService
**Retenciones** | https://e-factura.sunat.gob.pe/ol-ti-itemision-otroscpe-gem/billService
**Consulta validez** | https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService
**Consulta CDR** | https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService

# Enviar Comprobantes de Pago mediante BillService
Para enviar Boletas, Facturas, Notas de Crédito, Notas de Débito, Guias de Remisión, Percepciones, Retenciones se debe de usar la clase BillServiceManager.

El valor de URL dependerá de qué tipo de documento está intentando enviar.
```
String URL = "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService";
USERNAME = "12345678959MODDATOS"; // RUC + USUARIO SOL
PASSWORD = "MODDATOS"; // PASSWORD SOL

File file = new File(".../myFolder/12345678959-01-F001-00000001.xml");
byte[] result = BillServiceManager.sendBill(file, URL, USERNAME, PASSWORD);
```

# Enviar Resumenes diarios y Bajas mediante BillService
Para Enviar Resumenes diarios y Bajas se debe de usar la clase BillServiceManager.

```
String URL = "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService";
USERNAME = "12345678959MODDATOS"; // RUC + USUARIO SOL
PASSWORD = "MODDATOS"; // PASSWORD SOL

File file = new File(".../myFolder/12345678959-RA-20180316-00001.xml");

String ticket = BillServiceManager.sendSummary(file, URL, USERNAME, PASSWORD);
```