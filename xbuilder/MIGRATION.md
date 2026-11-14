# XBuilder 5.x → 6.0

XBuilder 6.0 creates and signs documents with the **xhandler core**, the Rust library
also used by the Go and Python libraries. It runs on the JVM as WebAssembly compiled to
bytecode (Chicory): no native code, and it works in GraalVM native images. The three
libraries produce the same XML and signatures.

## Building documents

| 5.x | 6.0 |
|---|---|
| `ContentEnricher enricher = new ContentEnricher(defaults, dateProvider); enricher.enrich(invoice);` then `TemplateProducer.getInstance().getInvoice().data(invoice).render()` | `new XBuilder().build(invoice)` |
| `enricher.enrich(invoice)` to read the computed values | `Invoice enriched = new XBuilder().enrich(invoice)` (a copy) |
| `Defaults.builder().igvTasa(..).icbTasa(..)` + `DateProvider` | `Defaults.builder().fechaEmision(..).igvTasa(..).icbTasa(..).ivapTasa(..)`; every field is optional: today in Peru and the current SUNAT rates |
| Quarkus: `XBuilder.getTemplate(Type.INVOICE)` | inject `io.github.project.openubl.xbuilder.XBuilder`; `quarkus.xbuilder.igv-tasa`, `icb-tasa`, `ivap-tasa` keep working |
| Invalid catalog codes | `XhandlerException` with `getKind() == ErrorKind.VALIDATION` |
| XML → models (JAXB mappers, `quarkus-jaxb`) | removed |

## Models

The models are generated from the core and live in
`io.github.project.openubl.xbuilder.models` (one package, Lombok builders). Their JSON
is the one of the Rust core (`snake_case`), so documents can be stored or exchanged with
the other libraries as `{"kind": "Invoice", "spec": {...}}`.

| 5.x | 6.0 |
|---|---|
| `content.models.standard.general.Invoice`, `CreditNote`, `DebitNote` | `models.Invoice`, `CreditNote`, `DebitNote` |
| `.serie("F001").numero(1)` | `.serieNumero("F001-1")` |
| `.detalle(d1).detalle(d2)` | `.detalles(List.of(d1, d2))` |
| `DocumentoVentaDetalle` | `Detalle` |
| `tasaIgv`, `tasaIcb`, `tasaIsc`, `tasaIvap` | `igvTasa`, `icbTasa`, `iscTasa`, `ivapTasa` |
| `formaDePago` / `FormaDePago` / `CuotaDePago` | `formaPago` / `FormaPago` / `CuotaPago` |
| `ordenDeCompra` | `ordenCompra` |
| `sustentoDescripcion` (notes) | `descripcionSustento` |
| `EmbededDespatch` | `GuiaEmbebida` |
| `CargoDescuento` | `Cargo`, `Descuento` |
| `standard.guia.*` (`Driver`, `Vehicle`, `Destino`, `GuiaItemAttribute`, `DocumentoBaja`...) | `Chofer`, `Vehiculo`, `Partida`, `DespatchAdviceAtributo`, `DespatchAdviceDocumentoBaja`... |
| `sunat.resumen.Comprobante`, `ComprobanteValorVenta`, `ComprobanteImpuestos` | `SummaryDocumentsItemComprobante`, `SummaryDocumentsValorVenta`, `SummaryDocumentsImpuestos` |
| `sunat.percepcionretencion.ComprobanteAfectado` | `PercepcionRetencionComprobanteAfectado` |
| `Catalog6.RUC.toString()` and the other catalog enums | the codes as strings, e.g. `"6"` |
| Numbers (`numero`) `Integer` | `Long` |

## Signing

| 5.x | 6.0 |
|---|---|
| `CertificateDetailsFactory.create(pfx, password)` + `XMLSigner.signXML(xml, "signatureId", cert, key)` | `new XSigner().sign(xml, KeyPair.fromPkcs12(pfxBytes, password))` |
| PEM keys | `KeyPair.fromPem(privateKey, certificate)` |
| A `java.security.KeyStore` | `KeyPair.fromKeyStore(keyStore, alias, password)` |
| `XmlSignatureHelper.getBytesFromDocument(signed)` read as ISO-8859-1 | `sign(byte[])` returns UTF-8 bytes; `sign(String)` returns text |

The signature id is always `PROJECT-OPENUBL-SIGN`, the one the generated XML references.

## XSender

`XSender` and `SunatClient` keep their API (see [the XSender guide](../xsender/MIGRATION.md))
and now use the same core: a guía de remisión REST error has its details in
`metadata.description` (`"Unprocessable Entity; 1033 - ..."`) instead of
`metadata.notes`.
