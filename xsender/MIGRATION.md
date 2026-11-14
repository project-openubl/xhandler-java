# Migración de XSender 5.x a 6.0

XSender 6.0 ya no usa Apache Camel ni Apache CXF. El envío a la SUNAT lo hace un cliente propio construido solo con el JDK, por lo que tu aplicación deja de cargar unas 70 librerías y ya no necesita un `CamelContext`.

En 6.0 la clase `XSender` se llama `FileSender` y sus métodos tienen los nombres de las
librerías de Go y Python: `send` → `sendFile`, `verifyTicket(xml, ticket)` →
`verifyFileTicket(xml, ticket)`, y `verifyTicket(destino, ticket)` consulta un ticket en un
servicio dado. `CompanyURLs` y `CompanyCredentials` se llaman `URLs` y `Credentials` (paquete
`io.github.project.openubl.xsender`, con `URLs.beta()` y `URLs.production()`), y
`XSenderException` ya no existe: ver los errores más abajo. En `SunatClient`, `consultDocument` es `getStatus` / `getStatusCdr` y las
consultas reciben la URL del servicio (`SunatClient.BILL_CONSULT_SERVICE_URL`,
`BILL_VALID_SERVICE_URL`) en lugar de `BillConsultServiceDestination` /
`BillValidServiceDestination`.

## Qué se eliminó

| 5.x | 6.0 |
|-----|-----|
| `CamelUtils`, `CamelData`, `ProducerTemplate` | `FileSender` / `SunatClient` |
| `StandaloneCamel` | `new FileSender(urls, credentials)` |
| `Constants.XSENDER_*_URI` (rutas `direct:`) | métodos de `SunatClient` |
| `direct:xsender-credentialsApi` (token de guías) | automático: `SunatClient` pide y reutiliza el token |
| Clases generadas `service.sunat.gob.pe.*` | `ConsultResponse`, `DocumentQuery`, `CdpQuery` |
| `spring.xsender.enable-logging-feature`, `quarkus.xsender.enable-logging-feature` | eliminados |

Se mantienen `URLs`, `Credentials`, `SunatResponse`, `Status`, `BillServiceXMLFileAnalyzer` y los `*Destination`, con estos cambios menores:

- `models.rest.*Dto` se eliminaron (eran internos de las rutas de Camel).
- `BillServiceDestination.RestOperation.getMethod()` se eliminó (devolvía un tipo de Camel).
- `CdrReader(byte[])` ahora lanza `IOException` y `XMLStreamException`; `ByteUtils.getDocumentFromBytes` se eliminó.

## Enviar un comprobante

Antes:

```java
BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xml, companyURLs);
CamelData camelData = CamelUtils.getBillServiceCamelData(
        fileAnalyzer.getZipFile(), fileAnalyzer.getSendFileDestination(), credentials
);
SunatResponse response = camelContext.createProducerTemplate().requestBodyAndHeaders(
        Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class
);
```

Ahora:

```java
FileSender xsender = new FileSender(companyURLs, credentials);
SunatResponse response = xsender.sendFile(xml);
```

## Consultar un ticket

```java
SunatResponse response = xsender.verifyFileTicket(xml, ticket);
```

`xml` es el mismo documento que se envió: FileSender lo usa para saber a qué servicio preguntar.

## Guías de remisión

Ya no necesitas pedir el token a mano. Agrega `clientId` y `clientSecret` a `Credentials` y FileSender pide el token, lo reutiliza hasta un minuto antes de que expire y lo renueva si la SUNAT lo rechaza. Si prefieres manejar el token tú mismo, sigue funcionando `Credentials.builder().token(...)`.

## Servicios de consulta y validación

```java
SunatClient client = new SunatClient();

ConsultResponse cdr = client.consultDocument(
        BillConsultServiceDestination.builder().url(url).operation(BillConsultServiceDestination.Operation.GET_STATUS_CDR).build(),
        new DocumentQuery("20123456789", "01", "F001", 102),
        credentials
);

ConsultResponse validation = client.validateCdp(
        new BillValidServiceDestination(url),
        new CdpQuery("20123456789", "01", "F001", "102", "6", "20987654321", "10/01/2024", new BigDecimal("118.00"), null),
        credentials
);
```

Antes devolvían las clases generadas por CXF y los errores llegaban como `CamelExecutionException`; ahora devuelven un `ConsultResponse` con el código y mensaje de la SUNAT (también para los SOAP faults).

## Errores

- Las respuestas de la SUNAT (CDR, rechazos, SOAP faults, errores de la API de guías) llegan como `SunatResponse`.
- Las respuestas que no son de la SUNAT y los documentos que no se pueden enviar lanzan `XhandlerException` (con su `ErrorKind`, p. ej. `RESPONSE` o `RATE_LIMITED`), los problemas de red `UncheckedIOException` y las URLs inválidas `IllegalArgumentException`.

## Spring Boot

- Elimina `@ComponentScan("io.github.project.openubl.spring.xsender.runtime")`: el starter ahora se autoconfigura.
- Las propiedades pasan de `spring.xsender.*` a `xsender.*`.
- Elimina las dependencias de Camel (`camel-spring-boot-bom`, `camel-test-spring-junit6`...).
- Inyecta `FileSender` (configurando `xsender.company.*`) o `SunatClient`.

## Quarkus

- Elimina el `quarkus-camel-bom` si solo lo usabas para XSender, y `quarkus.xsender.enable-logging-feature`.
- Inyecta `FileSender` (configurando `quarkus.xsender.company.*`) o `SunatClient`.

## WildFly / Jakarta EE

Agrega `xsender-cdi` e inyecta `FileSender` o `SunatClient`; la configuración se lee con MicroProfile Config (`xsender.company.*`).
