# XHandler Java

[![License](https://img.shields.io/github/license/project-openubl/xhandler?logo=apache)](https://www.apache.org/licenses/LICENSE-2.0)
[![CI](https://github.com/project-openubl/xhandler/actions/workflows/ci.yml/badge.svg)](https://github.com/project-openubl/xhandler/actions/workflows/ci.yml)

[![Project Chat](https://img.shields.io/badge/zulip-join_chat-brightgreen.svg?style=for-the-badge&logo=zulip)](https://projectopenubl.zulipchat.com/)
[![Supported JVM Versions](https://img.shields.io/badge/JVM--25-brightgreen.svg?style=for-the-badge&logo=Java)](https://github.com/project-openubl/xhandler/actions/)

**XHandler Java** es una suite de herramientas diseñada para facilitar la integración de **Facturación Electrónica en Perú (SUNAT)** en aplicaciones Java. Este repositorio es un "monorepo" que alberga las librerías `XBuilder` y `XSender`, proporcionando una solución integral para crear, firmar y enviar comprobantes de pago electrónicos.

> [!TIP]
> Si buscas integrar facturación electrónica de manera rápida y estándar, estás en el lugar correcto.

---

## 📦 Ecosistema

El proyecto se divide en módulos principales y extensiones para frameworks populares:

| Componente | Descripción | Maven Central |
|------------|-------------|---------------|
| **XBuilder** | Creación y firma de XMLs (UBL 2.1) | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/xbuilder)](https://search.maven.org/artifact/io.github.project-openubl/xbuilder/) |
| **XSender** | Envío de comprobantes a SUNAT/OSE | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/xsender)](https://search.maven.org/artifact/io.github.project-openubl/xsender/) |
| **XSender CDI** | Beans de XSender para WildFly / Jakarta EE / MicroProfile | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/xsender-cdi)](https://search.maven.org/artifact/io.github.project-openubl/xsender-cdi/) |
| **Quarkus XBuilder** | Extensión XBuilder para Quarkus | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/quarkus-xbuilder)](https://search.maven.org/artifact/io.github.project-openubl/quarkus-xbuilder/) |
| **Quarkus XSender** | Extensión XSender para Quarkus | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/quarkus-xsender)](https://search.maven.org/artifact/io.github.project-openubl/quarkus-xsender/) |
| **Spring Boot XSender** | Starter XSender para Spring Boot | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/spring-boot-xsender)](https://search.maven.org/artifact/io.github.project-openubl/spring-boot-xsender/) |

---

## 🛠️ XBuilder

XBuilder abstrae la complejidad de los estándares UBL y XML, permitiéndote construir documentos tributarios válidos escribiendo código Java simple.

### Características
- **Simple**: No necesitas manipular XML directamente ni conciliar namespaces complejos.
- **Completo**: Soporte para Facturas, Boletas, Notas de Crédito/Débito, Guías de Remisión y Percepciones/Retenciones.
- **Validado**: Realiza cálculos automáticos y validaciones básicas según normativa SUNAT.

### Ejemplo de Uso

```java
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
                .precio(new BigDecimal("100"))
                .build()))
        .build();

String xml = new XBuilder().build(invoice);       // fecha de hoy en Perú y tasas vigentes
byte[] signed = new XSigner().sign(xml.getBytes(StandardCharsets.UTF_8),
        KeyPair.fromPkcs12(Files.readAllBytes(Path.of("certificado.pfx")), "password"));
```

XBuilder y XSender usan el **núcleo xhandler** (Rust), el mismo de las librerías de Go y
Python, ejecutado en la JVM como WebAssembly compilado a bytecode
([Chicory](https://chicory.dev)): sin código nativo y compatible con GraalVM native.

> [!NOTE]
> ¿Vienes de XBuilder 5.x? Revisa la [guía de migración](./xbuilder/MIGRATION.md).

> [!NOTE]
> Los casos de prueba (`testdata/conformance`), el módulo WebAssembly y los modelos se
> copian desde xhandler-rust con `make sync RUST_REPO=../xhandler-rust`.

---

## 🚀 XSender

XSender envía tus comprobantes firmados a la SUNAT (o a un OSE): servicios SOAP (`billService`, consulta y validación) y la API REST de guías de remisión (con su token OAuth).

### Características
- **Sin dependencias**: solo usa el JDK (`java.net.http`, StAX, `java.util.zip`). Funciona en Java SE, Spring Boot, Quarkus (JVM y nativo), WildFly/Jakarta EE, Tomcat, etc.
- **Automático**: lee el XML para decidir el nombre del archivo, el servicio y la operación (`sendBill`, `sendSummary`, guía de remisión REST...).
- **Respuestas claras**: CDR, ticket o error de la SUNAT llegan como un `SunatResponse`; los fallos de red como `XSenderException`.

### Ejemplo de Uso

```java
XSender xsender = new XSender(
        CompanyURLs.builder()
                .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
                .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
                .build(),
        CompanyCredentials.builder()
                .username("12345678959MODDATOS")
                .password("MODDATOS")
                .build()
);

SunatResponse response = xsender.send(signedXml);   // byte[] o Path
if (response.isTicket()) {
    response = xsender.verifyTicket(signedXml, response.getSunat().getTicket());
}
System.out.println(response.getStatus());           // ACEPTADO, RECHAZADO, EXCEPCION, EN_PROCESO...
```

Para guías de remisión agrega `.despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")` a las URLs y `clientId`/`clientSecret` a las credenciales; la SUNAT no ofrece un entorno beta para ellas, así que necesitas credenciales reales.

Si envías comprobantes de varias empresas, comparte un solo `SunatClient` (conexiones y tokens) entre sus `XSender`: `new XSender(urls, credentials, sunatClient)`.

### Frameworks

| Framework | Dependencia | Configuración |
|-----------|-------------|---------------|
| Spring Boot | `spring-boot-xsender` | `xsender.company.*` en `application.properties` |
| Quarkus | `quarkus-xsender` | `quarkus.xsender.company.*` |
| WildFly / Jakarta EE / MicroProfile | `xsender-cdi` | `xsender.company.*` (MicroProfile Config) |

Todas exponen un `SunatClient` compartido y un `XSender` para la empresa configurada (en Quarkus y CDI, inyectar `XSender` sin configurar la empresa lanza `XSenderException`). Spring Boot / CDI (en Quarkus antepón `quarkus.`):

```properties
xsender.company.invoice-url=https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService
xsender.company.username=12345678959MODDATOS
xsender.company.password=MODDATOS
# xsender.company.perception-retention-url, despatch-url, client-id, client-secret
# xsender.security-api-url  (por defecto https://api-seguridad.sunat.gob.pe, útil para pruebas)
```

> [!NOTE]
> ¿Vienes de XSender 5.x (Apache Camel)? Revisa la [guía de migración](./xsender/MIGRATION.md).

---

## 💻 Ejemplos

Explora la carpeta `examples/` para ver implementaciones de referencia:

- [**Spring Boot**](./examples/springbot): Ejemplo de integración completa usando Spring Boot.
- [**Wildfly**](./examples/wildfly): Ejemplo para servidores de aplicaciones Jakarta EE.
- [**Tomcat**](./examples/tomcat): Ejemplo ligero desplegable en Tomcat.
- [**XBuilder/XSender**](./examples): Ejemplos "standalone" de uso de las librerías.

---

## 📚 Documentación

Para guías detalladas, referencia de API y tutoriales, consulta nuestra documentación oficial.

- 📖 **Sitio Web**: [project-openubl.github.io](https://project-openubl.github.io)
- 💬 **Comunidad**: [Únete al chat en Zulip](https://projectopenubl.zulipchat.com/)
- 🐛 **Soporte**: [Reportar un problema o discutir mejoras](https://github.com/project-openubl/xsender/discussions)

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia **Apache 2.0**. Consulta el archivo [LICENSE](LICENSE) para más detalles.

Copyright © Project OpenUBL.
