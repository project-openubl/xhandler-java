# XHandler Java

[![License](https://img.shields.io/github/license/project-openubl/xhandler?logo=apache)](https://www.apache.org/licenses/LICENSE-2.0)
[![CI](https://github.com/project-openubl/xhandler/actions/workflows/ci.yml/badge.svg)](https://github.com/project-openubl/xhandler/actions/workflows/ci.yml)

[![Project Chat](https://img.shields.io/badge/zulip-join_chat-brightgreen.svg?style=for-the-badge&logo=zulip)](https://projectopenubl.zulipchat.com/)
[![Supported JVM Versions](https://img.shields.io/badge/JVM--21-brightgreen.svg?style=for-the-badge&logo=Java)](https://github.com/project-openubl/xhandler/actions/)

Suite de librerías Java para **Facturación Electrónica en Perú (SUNAT)**: creación, firma y envío de comprobantes de pago electrónicos conforme a UBL 2.1.

> [!TIP]
> Si buscas integrar facturación electrónica SUNAT de manera rápida y estándar, estás en el lugar correcto.

---

## Requisitos

| Requisito     | Versión mínima |
|--------------|----------------|
| **Java**      | 21             |
| **Maven**     | 3.8+           |
| **Quarkus**   | 3.8+ (extensiones) |
| **Spring Boot** | 3.2+ (extensión)  |

---

## Ecosistema

| Componente | Descripción | Maven Central |
|------------|-------------|---------------|
| **XBuilder** | Creación y firma de XMLs (UBL 2.1) | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/xbuilder)](https://search.maven.org/artifact/io.github.project-openubl/xbuilder/) |
| **XSender** | Envío de comprobantes a SUNAT/OSE | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/xsender)](https://search.maven.org/artifact/io.github.project-openubl/xsender/) |
| **Quarkus XBuilder** | Extensión XBuilder para Quarkus | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/quarkus-xbuilder)](https://search.maven.org/artifact/io.github.project-openubl/quarkus-xbuilder/) |
| **Quarkus XSender** | Extensión XSender para Quarkus | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/quarkus-xsender)](https://search.maven.org/artifact/io.github.project-openubl/quarkus-xsender/) |
| **Spring Boot XSender** | Starter XSender para Spring Boot | [![Maven Central](https://img.shields.io/maven-central/v/io.github.project-openubl/spring-boot-xsender)](https://search.maven.org/artifact/io.github.project-openubl/spring-boot-xsender/) |

---

## Inicio rápido

### 1. Agregar dependencia

```xml
<dependency>
    <groupId>io.github.project-openubl</groupId>
    <artifactId>xbuilder</artifactId>
    <version>LATEST</version>
</dependency>
```

### 2. Crear una Factura

```java
var invoice = Invoice.builder()
        .serie("F001")
        .numero(1)
        .proveedor(Proveedor.builder()
                .ruc("20123456789")
                .razonSocial("Mi Empresa S.A.C.")
                .build())
        .cliente(Cliente.builder()
                .nombre("Cliente Ejemplo")
                .numeroDocumentoIdentidad("10467793549")
                .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                .build())
        .detalle(DocumentoVentaDetalle.builder()
                .descripcion("Servicio de consultoría")
                .cantidad(new BigDecimal("1"))
                .precio(new BigDecimal("500"))
                .build())
        .build();
```

### 3. Generar XML UBL 2.1

```java
var enricher = new ContentEnricher(defaults, dateProvider);
enricher.enrich(invoice);

Template template = TemplateProducer.getInstance().getInvoice();
String xml = template.data(invoice).render();
```

### 4. Enviar a SUNAT (con XSender)

```xml
<dependency>
    <groupId>io.github.project-openubl</groupId>
    <artifactId>xsender</artifactId>
    <version>LATEST</version>
</dependency>
```

---

## Documentos soportados

| Documento | Clase | Descripción |
|-----------|-------|-------------|
| Factura | `Invoice` | Ventas gravadas, exoneradas, inafectas, gratuitas |
| Boleta de Venta | `Invoice` | Con `tipoComprobante = "03"` |
| Nota de Crédito | `CreditNote` | Anulaciones, descuentos, devoluciones |
| Nota de Débito | `DebitNote` | Intereses, penalidades, ajustes |
| Guía de Remisión | `DespatchAdvice` | GRE-Remitente (09) y GRE-Transportista (31) |
| Resumen Diario | `SummaryDocuments` | Informar emisión de boletas |
| Comunicación de Baja | `VoidedDocuments` | Anular comprobantes emitidos |
| Percepción | `Perception` | Comprobantes de percepción |
| Retención | `Retention` | Comprobantes de retención |
| Reversión | `Reversion` | Anular percepciones/retenciones |

---

## Extensiones para frameworks

### Quarkus

```xml
<!-- XBuilder -->
<dependency>
    <groupId>io.github.project-openubl</groupId>
    <artifactId>quarkus-xbuilder</artifactId>
    <version>LATEST</version>
</dependency>

<!-- XSender -->
<dependency>
    <groupId>io.github.project-openubl</groupId>
    <artifactId>quarkus-xsender</artifactId>
    <version>LATEST</version>
</dependency>
```

Configuración en `application.properties`:
```properties
quarkus.xbuilder.igv-tasa=0.18
quarkus.xbuilder.icb-tasa=0.2
quarkus.xsender.enable-logging-feature=false
```

Compilación nativa con GraalVM soportada:
```bash
mvn package -Pnative
```

Pruebas unitarias y de integracion.
```bash
mvn clean install compile
mvn verify -Pexamples
mvn -Pnative-image install -f xbuilder/quarkus-extension/integration-tests/ -Dquarkus.version=3.12.0
mvn -Pnative-image install -f xbuilder/quarkus-extension/integration-tests/ -Dquarkus.version=3.8.6
mvn -Pnative-image install -f xbuilder/quarkus-extension/integration-tests/ 
mvn -Pnative-image install -f xsender/quarkus-extension/integration-tests/ -Dquarkus.version=3.12.0
mvn -Pnative-image install -f xsender/quarkus-extension/integration-tests/ -Dquarkus.version=3.8.6
mvn -Pnative-image install -f xsender/quarkus-extension/integration-tests/

mvn install -f xsender/spring-boot-extension/integration-tests/ -Dspringboot.version=3.3.0
mvn install -f xsender/spring-boot-extension/integration-tests/
mvn install -f xsender/spring-boot-extension/integration-tests/ -Dspringboot.version=3.2.0

```

### Spring Boot

```xml
<dependency>
    <groupId>io.github.project-openubl</groupId>
    <artifactId>spring-boot-xsender</artifactId>
    <version>LATEST</version>
</dependency>
```

---

## Estructura del proyecto

```
xhandler-java/
├── xbuilder/
│   ├── core/                  # Librería principal XBuilder
│   └── quarkus-extension/     # Extensión Quarkus para XBuilder
│       ├── deployment/
│       ├── runtime/
│       └── integration-tests/
├── xsender/
│   ├── core/                  # Librería principal XSender
│   ├── quarkus-extension/     # Extensión Quarkus para XSender
│   └── spring-boot-extension/ # Starter Spring Boot para XSender
└── examples/
    ├── xbuilder/              # Ejemplo standalone XBuilder
    ├── xsender/               # Ejemplo standalone XSender
    ├── springbot/             # Integración Spring Boot
    ├── tomcat/                # Despliegue en Tomcat
    └── wildfly/               # Despliegue en WildFly
```

---

## Dependencias principales

| Librería | Versión | Uso |
|----------|---------|-----|
| Fastjson2 | 2.0.49 | Serialización JSON |
| Quarkus Qute | 3.15.1 | Plantillas XML |
| Apache Camel | 4.4.0 | Rutas SOAP (XSender) |
| Lombok | 1.18.34 | Reducción de boilerplate |
| MapStruct | 1.5.5 | Mapeo XML-JAXB a modelos |
| JAXB | 4.0.5 | Parsing XML UBL |

---

## Desarrollo local

### Compilar y ejecutar tests

```bash
# Tests unitarios (xbuilder + xsender)
mvn clean verify

# Solo xbuilder core (278 tests)
cd xbuilder/core && mvn test -DskipSunat=true

# Extensión Quarkus (JVM)
mvn clean install -f xbuilder/quarkus-extension/integration-tests/

# Extensión Quarkus (nativa GraalVM)
mvn -Pnative-image install -f xbuilder/quarkus-extension/integration-tests/

# Spring Boot
mvn clean install -f xsender/spring-boot-extension/integration-tests/
```

### Actualizar snapshots de test

```bash
mvn clean test -Dxbuilder.snapshot.update
```

### Matriz de compatibilidad CI

El CI valida automáticamente estas combinaciones:

| Framework | Versiones probadas |
|-----------|--------------------|
| Quarkus   | 3.8.6, 3.12.0, 3.15.1 (actual) |
| Spring Boot | 3.2.0, 3.2.5 (actual), 3.3.0 |

---

## Contribuir

1. Fork del repositorio
2. Crear branch: `git checkout -b feature/mi-mejora`
3. Hacer commit: `git commit -m 'Agregar mejora'`
4. Push: `git push origin feature/mi-mejora`
5. Abrir Pull Request

### Comunidad

- [Chat en Zulip](https://projectopenubl.zulipchat.com/)
- [Reportar problemas](https://github.com/project-openubl/xsender/discussions)

---

## Licencia

Distribuido bajo licencia [Apache 2.0](LICENSE).

Copyright Project OpenUBL.
