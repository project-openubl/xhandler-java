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

The abstract bases (`Document`, `SalesDocument`, `Note`, `SunatDocument`,
`BasePercepcionRetencion`) are gone: every document implements the `Document`
interface, and `Reversion` no longer extends `VoidedDocuments` (it has the same fields).

| 5.x | 6.0 |
|---|---|
| `content.models.standard.general.Invoice`, `CreditNote`, `DebitNote` | `models.Invoice`, `CreditNote`, `DebitNote` |
| `.serie("F001").numero(1)` (Invoice, notes, DespatchAdvice) | `.serieNumero("F001-1")`; Perception and Retention keep `serie` + `numero`; summaries, voided documents and reversions only have `numero` |
| `.detalle(d1).detalle(d2)`, `.cargo(..)`, `.guia(..)`... (`@Singular`) | `.detalles(List.of(d1, d2))`, `.cargos(..)`, `.guias(..)`: lists only |
| `DocumentoVentaDetalle` | `Detalle` |
| `Detalle.precioConImpuestos(true)`: a flag, "`precio` includes taxes" | `precioConImpuestos(BigDecimal)`: the price with taxes. Give `precio` or `precioConImpuestos`; the other one is computed |
| `tasaIgv`, `tasaIcb`, `tasaIsc`, `tasaIvap` | `igvTasa`, `icbTasa`, `iscTasa`, `ivapTasa` |
| `formaDePago` / `FormaDePago` / `CuotaDePago` | `formaPago` / `FormaPago` / `CuotaPago` |
| `FormaPago.tipo` (`String`) | the enum `TipoFormaPago` (`CREDITO`, `CONTADO`) |
| `Detraccion.medioDePago` | `medioPago` |
| `ordenDeCompra` | `ordenCompra` |
| `sustentoDescripcion` (notes) | `descripcionSustento` |
| `EmbededDespatch` | `GuiaEmbebida`, with `llegada` → `destino`, `nroLicencia` → `licencia`, `transpPlaca` → `placa`, `modTraslado` → `tipoModalidadTraslado`, `undPesoBruto` → `pesoBrutoUnidadMedida`; `transpCodeAuth` and `transpMarca` are removed |
| `CargoDescuento` | `Cargo`, `Descuento`: `porcentaje` → `factor`, `tipoDescuento` → `tipo`, new `montoBase`, no `serieNumero`; `cargos` only exist in invoices |
| `TotalImporteNote.importeConImpuestos` | removed |
| `standard.guia.Driver`, `Vehicle` | `Chofer`, `Vehiculo` |
| `standard.guia.Comprador`, `Tercero` | `Destinatario` |
| `standard.guia.Proveedor` | the shared `Proveedor` |
| `standard.guia.Destino` | `Partida` (`Envio.destino` keeps its name) |
| `standard.guia.GuiaItemAttribute` (`code`, `name`, `value`) | `DespatchAdviceAtributo` (`codigo`, `nombre`, `valor`) |
| `standard.guia.DocumentoBaja`, `DocumentoRelacionado` (`tipoDocumento`) | `DespatchAdviceDocumentoBaja` (`tipoComprobante`), `DespatchAdviceDocumentoRelacionado` |
| `Guia.tipoDocumento` | `tipoComprobante` |
| `numeroRegistroMTC`, `Envio.numeroDeBultos` | `numeroRegistroMtc`, `numeroBultos` |
| `sunat.resumen.Comprobante`, `ComprobanteValorVenta`, `ComprobanteImpuestos`, `ComprobanteAfectado` | `SummaryDocumentsItemComprobante`, `SummaryDocumentsValorVenta`, `SummaryDocumentsImpuestos`, `SummaryDocumentsComprobanteAfectado` |
| `sunat.resumen.SummaryPerception` (`codReg`, `tasa`, `mtoBase`, `mto`, `mtoTotal`) | `SummaryDocumentsPercepcion` (`tipoRegimen`, `porcentaje`, `montoBase`, `monto`, `montoTotal`) |
| The client of a summary item | `SummaryDocumentsCliente`, without `nombre` |
| `sunat.percepcionretencion.ComprobanteAfectado` | `PercepcionRetencionComprobanteAfectado` |
| `PercepcionRetencionOperacion.comprobante` | `comprobanteAfectado` |
| Perception / Retention `observacion` | `observaciones` |
| `Catalog6.RUC.toString()` and the other catalog enums | the codes as strings, e.g. `"6"` (only `FormaPago.tipo` is an enum) |
| Numbers (`numero`) `Integer` | `Long` |

## Signing

| 5.x | 6.0 |
|---|---|
| `CertificateDetailsFactory.create(pfx, password)` + `XMLSigner.signXML(xml, "signatureId", cert, key)` | `new XSigner().sign(xml, SigningKey.fromPkcs12(pfxBytes, password))` |
| PEM keys | `SigningKey.fromPem(privateKey, certificate)` |
| A `java.security.KeyStore` | `SigningKey.fromKeyStore(keyStore, alias, password)` |
| `XmlSignatureHelper.getBytesFromDocument(signed)` read as ISO-8859-1 | `sign(byte[])` returns UTF-8 bytes; `sign(String)` returns text |

The signature id is always `PROJECT-OPENUBL-SIGN`, the one the generated XML references.

## XSender

The `XSender` class is now `FileSender`, with the method names of the Go and Python
libraries (`sendFile`, `sendPack`, `verifyTicket(destination, ticket)`,
`verifyFileTicket(xml, ticket)`), and `SunatClient` consults take the service URL (see
[the XSender guide](../xsender/MIGRATION.md)). New: `CompanyURLs.beta()` /
`production()` and `XSenderException.getKind()`. Both use the same core: a guía de remisión REST error has its details in
`metadata.description` (`"Unprocessable Entity; 1033 - ..."`) instead of
`metadata.notes`.
