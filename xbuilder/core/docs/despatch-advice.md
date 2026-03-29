# xbuilder — Guía de Remisión Electrónica (GRE)

Módulo de construcción, validación y renderizado XML de Guías de Remisión Electrónica
para SUNAT. Soporta los dos tipos definidos por RS 000123-2022/SUNAT:

| Tipo | Código | Serie | Emisor                     |
|------|--------|-------|----------------------------|
| GRE-Remitente      | 09 | Txxx | Remitente de los bienes    |
| GRE-Transportista  | 31 | Vxxx | Transportista contratado   |

## Arquitectura

```
content/models/standard/guia/
├── DespatchAdvice.java           # Modelo principal UBL
├── GRERemitente.java             # Wrapper tipo 09 con validación y conversión
├── GRETransportista.java         # Wrapper tipo 31 con validación y conversión
├── DespatchAdviceValidator.java  # Validador estático para DespatchAdvice
├── validation/
│   ├── DespatchAdviceCommonValidator.java  # Reglas compartidas centralizadas
│   ├── ValidationSeverity.java   # ERROR | WARNING
│   ├── ValidationMessage.java    # Mensaje con severidad
│   └── ValidationResult.java     # Resultado agregado (errores + advertencias)
├── Envio.java                    # Datos de shipment
├── Remitente.java                # Datos del remitente (RUC + razón social)
├── Transportista.java            # Datos del transportista
├── Destinatario.java             # Destinatario de los bienes
├── Tercero.java                  # Remitente original (para tipo 31)
├── Driver.java                   # Conductor/chofer
├── Vehicle.java                  # Vehículo (principal + secundarios)
├── Partida.java                  # Punto de partida (UBIGEO + dirección)
├── Destino.java                  # Punto de destino
├── DespatchAdviceItem.java       # Línea de detalle (bien trasladado)
├── Contenedor.java               # Contenedor (comercio exterior)
├── DeclaracionAduanera.java      # DAM/DS (comercio exterior)
├── Puerto.java                   # Puerto/aeropuerto
├── DocumentoBaja.java            # Referencia a GRE anulada
├── DocumentoRelacionado.java     # Documento relacionado (Cat. 21)
├── DocumentoAdicional.java       # Documento adicional (Cat. 61)
└── GuiaItemAttribute.java        # Atributo adicional de ítem
```

## Flujo de uso

```java
// 1. Construir modelo
GRERemitente gre = GRERemitente.builder()
        .serie("T001").numero(1)
        .remitente(Remitente.builder().ruc("20100010001").razonSocial("Mi Empresa").build())
        .destinatario(Destinatario.builder()
                .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20200020002")
                .nombre("Cliente S.A.C.").build())
        .envio(Envio.builder()
                .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
                .chofer(Driver.builder().tipoDocumentoIdentidad("1")
                        .numeroDocumentoIdentidad("12345678").nombres("Juan")
                        .apellidos("Perez").licencia("Q123").build())
                .vehiculo(Vehicle.builder().placa("ABC-123").build())
                .partida(Partida.builder().ubigeo("150101").direccion("Av. Origen 100").build())
                .destino(Destino.builder().ubigeo("150102").direccion("Av. Destino 200").build())
                .build())
        .detalle(DespatchAdviceItem.builder()
                .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
        .build();

// 2. Validar (opción A: solo errores)
List<String> errors = gre.validate();

// 2b. Validar (opción B: errores + advertencias)
ValidationResult result = gre.validateDetailed();
result.getErrors();    // List<String> — bloquean emisión
result.getWarnings();  // List<String> — recomendaciones

// 3. Convertir a DespatchAdvice (valida automáticamente)
DespatchAdvice da = gre.toDespatchAdviceValidated();

// 4. Enriquecer
contentEnricher.enrich(da);

// 5. Renderizar XML
Template template = TemplateProducer.getInstance().getDespatchAdvice();
String xml = template.data(da).render();
```

## Validación

### Validador centralizado

Toda la lógica de validación compartida vive en `DespatchAdviceCommonValidator`. Los tres
puntos de entrada (`DespatchAdviceValidator.validate()`, `GRERemitente.validate()`,
`GRETransportista.validate()`) delegan a este validador común para evitar duplicación.

### Criterio de severidad

| Severidad | Significado | Ejemplo |
|-----------|-------------|---------|
| **ERROR** | SUNAT rechaza el documento | Serie T* con tipo 31, falta conductor |
| **WARNING** | Recomendación normativa, no bloquea | Comercio exterior sin DAM/DS |

Las advertencias surgen principalmente de las reglas de comercio exterior
(RS 000240-2024/SUNAT) cuya obligatoriedad fue pospuesta al 01-jul-2026
por RS 000133-2025/SUNAT.

### Reglas comunes (centralizadas)

| Regla | Aplica a | Severidad |
|-------|----------|-----------|
| Serie requerida | Todos | ERROR |
| Número > 0 | Todos | ERROR |
| Serie T* para tipo 09 | DespatchAdvice, GRERemitente | ERROR |
| Serie V* para tipo 31 | DespatchAdvice, GRETransportista | ERROR |
| Remitente con RUC 11 dígitos | Todos | ERROR |
| Destinatario requerido | Todos | ERROR |
| Envío requerido (motivo, peso, modalidad, fecha) | Todos | ERROR |
| Partida y destino requeridos | Todos | ERROR |
| Al menos 1 línea de detalle | Todos | ERROR |
| Comercio exterior: DAM/DS recomendado | Todos | WARNING |
| Comercio exterior: puerto/aeropuerto recomendado | Todos | WARNING |

### Reglas específicas GRE-Remitente (tipo 09)

| Regla | Severidad |
|-------|-----------|
| Transporte privado (02): conductor y vehículo obligatorios (salvo M1/L) | ERROR |
| Transporte privado: NO transportista externo | ERROR |
| Transporte público (01): transportista obligatorio | ERROR |

### Reglas específicas GRE-Transportista (tipo 31)

| Regla | Severidad |
|-------|-----------|
| TransportistaEmisor con RUC 11 dígitos | ERROR |
| Tercero (remitente original) obligatorio | ERROR |
| Conductor(es) siempre obligatorio(s) | ERROR |
| Vehículo siempre obligatorio | ERROR |

## Tipos 09 vs 31

| Aspecto | GRE-Remitente (09) | GRE-Transportista (31) |
|---------|-------------------|----------------------|
| Serie | Txxx | Vxxx |
| Emisor del XML | Remitente de bienes | Transportista |
| `DespatchSupplierParty` | Remitente | TransportistaEmisor |
| `SellerSupplierParty` | — | Tercero (remitente original) |
| Conductor/vehículo | Según modalidad | Siempre obligatorio |
| Transportista en envío | Solo en transporte público | — |

## Comercio exterior (RS 000240-2024)

Para motivos de traslado 08, 09, 10, 19:

- `Envio.declaracionesAduaneras`: lista de `DeclaracionAduanera` (DAM/DS)
- `Envio.puerto` / `Envio.aeropuerto`: puerto o aeropuerto
- `Envio.contenedores`: lista de `Contenedor` con número y precinto

> **Nota**: La obligatoriedad del motivo 19 y la derogación del ticket de salida
> fue pospuesta al 01-jul-2026 por RS 000133-2025/SUNAT.

## Enriquecimiento (enricher)

El `ContentEnricher` aplica reglas automáticas post-construcción:

- `DespatchAdviceTipoComprobanteRule`: si `tipoComprobante` es null, lo deduce
  de la serie (T* → "09", V* → "31")
- `FechaEmisionRule`: completa `fechaEmision` si es null
- `FirmanteRule`: completa `firmante` si es null

## Normativa de referencia

| Resolución | Contenido |
|------------|-----------|
| RS 000123-2022/SUNAT | Reglas base GRE-Remitente y GRE-Transportista |
| RS 000240-2024/SUNAT | Comercio exterior: DAM/DS, contenedores, puertos |
| RS 000133-2025/SUNAT | Prórroga: motivo 19 y ticket de salida al 01-jul-2026 |
