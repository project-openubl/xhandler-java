# XHandler Java

[![License](https://img.shields.io/github/license/project-openubl/xhandler-java?logo=apache)](https://www.apache.org/licenses/LICENSE-2.0)
[![CI](https://github.com/project-openubl/xhandler-java/actions/workflows/ci.yml/badge.svg)](https://github.com/project-openubl/xhandler-java/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/xbuilder)](https://central.sonatype.com/namespace/io.github.project-openubl)
[![Project Chat](https://img.shields.io/badge/zulip-join_chat-brightgreen.svg?logo=zulip)](https://projectopenubl.zulipchat.com/)

Crea, firma y envía los comprobantes electrónicos (XML UBL) de la SUNAT del Perú desde Java.

| Librería | Qué hace |
|---|---|
| `xbuilder` | Crea el XML de facturas, boletas, notas de crédito y débito, guías de remisión, percepciones, retenciones, resúmenes diarios, comunicaciones de baja y reversiones. Solo das los datos esenciales: los valores por defecto, impuestos y totales se calculan. También firma el XML (RSA-SHA256, certificados PEM o PKCS#12) |
| `xsender` | Envía a la SUNAT (servicios SOAP y API REST de guías) y lee el CDR, tickets y errores |
| `quarkus-xbuilder`, `quarkus-xsender` | Extensiones para Quarkus (JVM y nativo) |
| `spring-boot-xsender` | Starter para Spring Boot |
| `xsender-cdi` | Beans para WildFly / Jakarta EE / MicroProfile |

Usa el [núcleo xhandler](https://github.com/project-openubl/xhandler-rust), el mismo de las
librerías de [Go](https://github.com/project-openubl/xhandler-go) y
[Python](https://github.com/project-openubl/xhandler-python), ejecutado en la JVM como WebAssembly
compilado a bytecode: sin código nativo y compatible con GraalVM native.

## Instalación

Java 25 o superior.

```xml
<dependency>
    <groupId>io.github.project-openubl</groupId>
    <artifactId>xbuilder</artifactId>
    <version>6.0.0</version>
</dependency>
<dependency>
    <groupId>io.github.project-openubl</groupId>
    <artifactId>xsender</artifactId>
    <version>6.0.0</version>
</dependency>
```

> [!NOTE]
> 6.0.0 es la próxima versión. La última publicada, 5.1.1, tiene la API anterior: para pasar de
> 5.x a 6.0 revisa las guías de migración de [XBuilder](xbuilder/MIGRATION.md) y
> [XSender](xsender/MIGRATION.md).

## Uso

```java
// Crear
Invoice invoice = Invoice.builder()
        .serieNumero("F001-1")
        .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
        .cliente(Cliente.builder()
                .tipoDocumentoIdentidad("6") // RUC (Catalog6)
                .numeroDocumentoIdentidad("12121212121")
                .nombre("Carlos Feria")
                .build())
        .detalles(List.of(Detalle.builder()
                .descripcion("Item")
                .cantidad(new BigDecimal("2"))
                .precio(new BigDecimal("100.00"))
                .build()))
        .build();
String xml = new XBuilder().build(invoice); // fecha de hoy en Perú y tasas vigentes

// Firmar
SigningKey key = SigningKey.fromPkcs12(Files.readAllBytes(Path.of("certificado.pfx")), "password");
byte[] signed = new XSigner().sign(xml.getBytes(StandardCharsets.UTF_8), key);

// Enviar
FileSender sender = new FileSender(
        URLs.beta(),
        Credentials.builder().username("12345678959MODDATOS").password("MODDATOS").build()
);
SunatResponse response = sender.sendFile(signed);
if (response.isTicket()) { // resúmenes, bajas y guías: consulta el ticket después
    response = sender.verifyFileTicket(signed, response.getSunat().getTicket());
}
System.out.println(response.getStatus()); // ACEPTADO, RECHAZADO, EXCEPCION, EN_PROCESO...
```

- **Crear**: `new XBuilder().enrich(invoice)` devuelve el documento con los valores calculados
  (impuestos, totales, leyendas) en lugar del XML. `Defaults` cambia la fecha de emisión o las
  tasas.
- **Firmar**: `SigningKey.fromPem(llavePrivada, certificado)` o
  `SigningKey.fromKeyStore(keyStore, alias, password)`.
- **Enviar**: `sendPack` envía varias facturas en un zip. `SunatClient` también tiene los
  servicios de consulta y validación (`getStatus`, `getStatusCdr`, `validateCdp`,
  `validateFile`). Si envías por varias empresas, comparte un `SunatClient` (conexiones y tokens)
  entre sus `FileSender`.
- **Guías de remisión**: usan `URLs.production()` (la SUNAT no tiene beta para ellas) y el
  `clientId`/`clientSecret` de la API.
- **Errores**: los del núcleo son `XhandlerException` con su `ErrorKind` (p. ej. `VALIDATION` para
  un código de catálogo inválido) y los de red `UncheckedIOException`. Los rechazos de la SUNAT no
  son excepciones: llegan en el `SunatResponse`.

### Frameworks

| Framework | Dependencia | Configuración |
|---|---|---|
| Spring Boot | `spring-boot-xsender` | `xsender.company.*` |
| Quarkus | `quarkus-xbuilder`, `quarkus-xsender` | `quarkus.xbuilder.*`, `quarkus.xsender.company.*` |
| WildFly / Jakarta EE / MicroProfile | `xsender-cdi` | `xsender.company.*` (MicroProfile Config) |

Exponen un `SunatClient` compartido y un `FileSender` para la empresa configurada (Quarkus también
un `XBuilder`):

```properties
xsender.company.invoice-url=https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService
xsender.company.username=12345678959MODDATOS
xsender.company.password=MODDATOS
# xsender.company.perception-retention-url, despatch-url, client-id, client-secret
```

Ejemplos completos en [`examples/`](examples): Spring Boot, WildFly, Tomcat y Java SE.

## Contribuir

Revisa [CONTRIBUTING.md](CONTRIBUTING.md). Preguntas e ideas en el
[chat de Zulip](https://projectopenubl.zulipchat.com/).

## Licencia

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
