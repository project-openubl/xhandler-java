[![Build Status](https://travis-ci.org/carlosthe19916/sunat-web-services.svg?branch=master)](https://travis-ci.org/carlosthe19916/sunat-web-services)
[![Coverage Status](https://coveralls.io/repos/github/carlosthe19916/sunat-web-services/badge.svg?branch=master)](https://coveralls.io/github/carlosthe19916/sunat-web-services?branch=master)
[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=sunat-web-services&metric=alert_status)](https://sonarcloud.io/dashboard?id=sunat-web-services)

# SUNAT Web Services
Libreria para realizar envíos de comprobantes electrónicos a los servicios web de la SUNAT y/o OSCE de acuerdo a lo especificado por la SUNAT: 
![logo](./docs/images/sunat-logo20.png)


## Inicio rápido 

Incluir la última version de la libreria. Si utiliza maven agrege las siguientes lineas al archivo pom.xml:

```
<dependency>
    <groupId>io.github.carlosthe19916</groupId>
    <artifactId>sunat-web-services</artifactId>
    <version>latestVersion</version>
</dependency>
```

Puede ver la lista de todos las disponibles [aqui](https://mvnrepository.com/artifact/io.github.carlosthe19916/sunat-web-services).

### BillService:sendBill
Utilizado para enviar:

- Boletas y Facturas.
- Notas de Crédito.
- Notas de Débito. 
- Guias de Remisión.
- Percepciones.
- Retenciones.

```
ServiceConfig config = new ServiceConfig.Builder()
            .url("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .build();
            
File file = new File("../../12345678959-01-F001-00000001.xml");
BillServiceModel result = BillServiceManager.sendBill(file, config);
```

Nota: El valor de URL dependerá de qué tipo de documento está intentando enviar.

### BillService:sendSummary
Utilizado para enviar:

- Baja.
- Resumen diario.


```
ServiceConfig config = new ServiceConfig.Builder()
            .url("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .build();
            
File file = new File(".../../12345678959-RA-20180316-00001.xml");
BillServiceModel result = BillServiceManager.sendSummary(file, config);
```

Nota: El resultado de enviar una baja o resumen diario es un número de ticket. En caso desee consultar el ticket al mismo tiempo en el que se envía el comprobante, entonces deberás de usar:

```
ServiceConfig config;
File file;
Map<String, Object> params; // Datos que se enviarán al callback.
long delay; // Cantidad de milisegundos a esperar antes de consultar el ticket.

BillServiceManager.sendSummary(
    file, 
    config, 
    params, 
    new BillServiceCallback() {
         @Override
         public void onSuccess(Map<String, Object> params, int code, String description, byte[] cdr) { }

         @Override
         public void onError(Map<String, Object> params, int code, String description, byte[] cdr) { }

         @Override
         public void onProcess(Map<String, Object> params, int code, String description) { }

         @Override
         public void onException(Map<String, Object> params, int code, String description) { }

         @Override
         public void onThrownException(Map<String, Object> params, SOAPFaultException exception) { }
     }, delay);
```

### BillService:getStatus
Utilizado para consultar tickets:

```
ServiceConfig config = new ServiceConfig.Builder()
            .url("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .build();
            
BillServiceModel result = BillServiceManager.getStatus("miTicket", config);
```

### BillConsultService:getStatus
```
ServiceConfig config = new ServiceConfig.Builder()
                .url(URL_CONSULTA)
                .username(USERNAME)
                .passwod(PASSWORD)
                .build();

BillConsultBean consult = new BillConsultBean.Builder()
        .ruc("1234567894")
        .tipo("01")
        .serie("F001")
        .numero(102)
        .build();

service.sunat.gob.pe.billconsultservice.StatusResponse response = BillConsultServiceManager.getStatus(consult, config);
```

### BillConsultService:getStatusCdr
```
ServiceConfig config = new ServiceConfig.Builder()
                .url(URL_CONSULTA)
                .username(USERNAME)
                .passwod(PASSWORD)
                .build();

BillConsultBean consult = new BillConsultBean.Builder()
        .ruc("1234567894")
        .tipo("01")
        .serie("F001")
        .numero(102)
        .build();

service.sunat.gob.pe.billconsultservice.StatusResponse response = BillConsultServiceManager.getStatusCdr(config, consult);
```

### BillValidService:getStatus
Verificar la autenticidad de un archivo XMl:

```

File file = new File("../folder/F001-00005954.xml");

ServiceConfig config = new ServiceConfig.Builder()
    .url("https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService")
    .username(USERNAME)
    .passwod(PASSWORD)
    .build();

StatusResponse status = BillValidServiceManager.getStatus(file, config);
```

## Sunat Web Services

El conjunto de Web Services que la SUNAT publica puede ser encontrada [aqui](http://orientacion.sunat.gob.pe/index.php/empresas-menu/comprobantes-de-pago-empresas/comprobantes-de-pago-electronicos-empresas/see-desde-los-sistemas-del-contribuyente/988-guias-manuales-y-servicios-web). 


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

## 