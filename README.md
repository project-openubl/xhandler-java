# XHandler Java

[![License](https://img.shields.io/github/license/project-openubl/xhandler?logo=apache)](https://www.apache.org/licenses/LICENSE-2.0)
[![CI](https://github.com/project-openubl/xhandler/actions/workflows/ci.yml/badge.svg)](https://github.com/project-openubl/xhandler/actions/workflows/ci.yml)

[![Project Chat](https://img.shields.io/badge/zulip-join_chat-brightgreen.svg?style=for-the-badge&logo=zulip)](https://projectopenubl.zulipchat.com/)
[![Supported JVM Versions](https://img.shields.io/badge/JVM--17-brightgreen.svg?style=for-the-badge&logo=Java)](https://github.com/project-openubl/xhandler/actions/)

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
// Ejemplo simplificado de creación de factura
Invoice invoice = Invoice.builder()
    .serie("F001")
    .numero(1)
    .proveedor(proveedor)
    .cliente(cliente)
    .detalle(detalle)
    .build();

XMLInvoice xml = new InvoiceXMLBuilder().build(invoice);
```

> [!NOTE]
> Para actualizar los snapshots de prueba en desarrollo local, ejecuta:
> `mvn clean test -Dxbuilder.snapshot.update`

---

## 🚀 XSender

XSender se encarga de la comunicación con los servicios SOAP de la SUNAT o de los Operadores de Servicios Electrónicos (OSE).

### Características
- **Compatible**: Soporta los diversos endpoints de SUNAT (Beta/Producción) y OSEs.
- **Resiliente**: Gestiona el envío de archivos ZIP y el procesamiento de respuestas (CDR, Tickets).
- **Flexible**: Fácil integración con frameworks modernos como Quarkus y Spring Boot.

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
