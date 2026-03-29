# xbuilder — Validation Architecture

## Overview

The validation subsystem provides early detection of business rule violations
before submitting GRE documents to SUNAT. It does **not** replace SUNAT's
XSD/XSL validation; it is a complementary layer.

## Design Principles

### Centralized Rules (DRY)

All shared validation rules live in a single class:

```
validation/DespatchAdviceCommonValidator.java
```

The three entry points delegate to it:

```
DespatchAdviceValidator.validate(DespatchAdvice)    → common + generic-type rules
GRERemitente.validate()                              → common + remitente rules
GRETransportista.validate()                          → common + transportista rules
```

This eliminates the prior duplication where the same rule (e.g., "serie required",
"RUC must be 11 digits") was coded in 3 places with risk of functional drift.

### Severity Levels

```java
public enum ValidationSeverity {
    ERROR,    // SUNAT will reject the document
    WARNING   // Recommendation; not blocking
}
```

**Criteria for ERROR**:
- Fields required by UBL schema
- Fields required by SUNAT functional rules (RS 000123-2022)
- Coherence rules (serie prefix vs. tipo comprobante)

**Criteria for WARNING**:
- Commerce exterior recommendations (DAM/DS, puerto) whose mandatory enforcement
  is deferred by RS 000133-2025/SUNAT to 01-jul-2026
- Fields that depend on business context

### Backward Compatibility

The `validate()` methods continue returning `List<String>` (errors only).
A new `validateDetailed()` method returns `ValidationResult` with both
errors and warnings.

```java
// Old API (unchanged)
List<String> errors = gre.validate();

// New API
ValidationResult result = gre.validateDetailed();
result.getErrors();      // List<String>
result.getWarnings();    // List<String>
result.hasErrors();      // boolean
result.isValid();        // boolean (no errors)
```

## Class Diagram

```
┌──────────────────────────────────┐
│  DespatchAdviceCommonValidator   │  ← All shared rules
│  ─────────────────────────────   │
│  + validateBasicFields()         │
│  + validateSerieCoherence()      │
│  + validateSerieRemitente()      │
│  + validateSerieTransportista()  │
│  + validateRemitente()           │
│  + validateTransportistaEmisor() │
│  + validateDestinatario()        │
│  + validateTerceroTransportista()│
│  + validateEnvioRequired()       │
│  + validatePartidaDestino()      │
│  + validateModalidadRemitente()  │
│  + validateModalidadGeneric()    │
│  + validateConductorVehiculo...()│
│  + validateComercioExterior()    │
│  + validateDetalles()            │
└──────────┬───────────────────────┘
           │ delegates to
    ┌──────┼──────────────┐
    │      │              │
    ▼      ▼              ▼
┌────────┐ ┌──────────┐  ┌───────────────┐
│Dispatch│ │GRERemit. │  │GRETransport.  │
│Advice  │ │.validate │  │.validate()    │
│Validat.│ │()        │  │               │
└────────┘ └──────────┘  └───────────────┘
```

## Adding New Rules

1. Add the method to `DespatchAdviceCommonValidator` if the rule is shared.
2. Call it from the relevant entry points.
3. Choose the correct severity (`ERROR` or `WARNING`).
4. Add corresponding unit tests to the existing test classes.
5. Update `despatch-advice.md` rule tables.
