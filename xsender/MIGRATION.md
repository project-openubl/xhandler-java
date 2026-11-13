# Migración de XSender 5.x a 6.0

XSender 6.0 ya no usa Apache Camel ni Apache CXF. El envío a la SUNAT lo hace un cliente propio construido solo con el JDK, por lo que tu aplicación deja de cargar unas 70 librerías y ya no necesita un `CamelContext`.

## Qué se eliminó

| 5.x | 6.0 |
|-----|-----|
| `CamelUtils`, `CamelData`, `ProducerTemplate` | `XSender` / `SunatClient` |
| `StandaloneCamel` | `new XSender(urls, credentials)` |
| `Constants.XSENDER_*_URI` (rutas `direct:`) | métodos de `SunatClient` |
| `direct:xsender-credentialsApi` (token de guías) | automático: `SunatClient` pide y reutiliza el token |
| Clases generadas `service.sunat.gob.pe.*` | `ConsultResponse`, `DocumentQuery`, `CdpQuery` |
| `spring.xsender.enable-logging-feature`, `quarkus.xsender.enable-logging-feature` | eliminados |

Se mantienen `CompanyURLs`, `CompanyCredentials`, `SunatResponse`, `Status`, `BillServiceXMLFileAnalyzer` y los `*Destination`.

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
XSender xsender = new XSender(companyURLs, credentials);
SunatResponse response = xsender.send(xml);
```

## Consultar un ticket

```java
SunatResponse response = xsender.verifyTicket(xml, ticket);
```

`xml` es el mismo documento que se envió: XSender lo usa para saber a qué servicio preguntar.

## Guías de remisión

Ya no necesitas pedir el token a mano. Agrega `clientId` y `clientSecret` a `CompanyCredentials` y XSender pide el token, lo reutiliza hasta un minuto antes de que expire y lo renueva si la SUNAT lo rechaza. Si prefieres manejar el token tú mismo, sigue funcionando `CompanyCredentials.token(...)`.

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
- Los problemas de red, URLs inválidas o respuestas que no son de la SUNAT lanzan `XSenderException` (unchecked).

## Spring Boot

- Elimina `@ComponentScan("io.github.project.openubl.spring.xsender.runtime")`: el starter ahora se autoconfigura.
- Elimina las dependencias de Camel (`camel-spring-boot-bom`, `camel-test-spring-junit6`...).
- Inyecta `XSender` (configurando `xsender.company.*`) o `SunatClient`.

## Quarkus

- Elimina el `quarkus-camel-bom` si solo lo usabas para XSender.
- Inyecta `XSender` (configurando `quarkus.xsender.company.*`) o `SunatClient`.

## WildFly / Jakarta EE

Agrega `xsender-cdi` e inyecta `XSender` o `SunatClient`; la configuración se lee con MicroProfile Config (`xsender.company.*`).
