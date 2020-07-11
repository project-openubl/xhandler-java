---
id: concepts
title: Concepts
---

XBuilder creates XML files based on 3 objects:

- **Config** - Default values to apply in case those values are not defined by the user.
- **System clock** - A clock which should give the current system time and the time zone.
- **POJO**- The POJO which contains the data to be used to create the XML file.

Those 3 objects are later used when creating a XML file. E.g.

```java
DocumentWrapper<InvoiceOutputModel> result = DocumentManager.createXML(pojo, config, systemClock);
```

## Config

You have two options for creating instances of `Config`:

- Use the `DefaultConfig` Class which implements `Config`.
- Implement your own version using the inheritance features of Java.

### Create your own Config

Implement the interface `Config`:

```java
public class MyCustomConfig implements Config {

    private BigDecimal igv;
    private BigDecimal ivap;
    private String defaultMoneda;
    private String defaultUnidadMedida;
    private Catalog9 defaultTipoNotaCredito;
    private Catalog10 defaultTipoNotaDebito;
    private BigDecimal defaultIcb;
    private Catalog7 defaultTipoIgv;

    public DefaultConfig() {
        this.defaultMoneda = "PEN";
        this.defaultUnidadMedida = "NIU";
        this.igv = new BigDecimal("0.18");
        this.ivap = new BigDecimal("0.04");
        this.defaultIcb = new BigDecimal("0.2");
        ...
    }
}
```

## SystemClock

To create a SystemClock class you need to implement `SystemClock` interface:

```java
 public class MyCustomClock implements SystemClock {
    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone("America/Lima");
    }

    @Override
    public Calendar getCalendarInstance() {
        return Calendar.getInstance();
    }
}
```

## POJO

The POJO is the Input Data that will be used to create the XML file based on UBL standards.

There are different types of POJOs depending of which type of XML you want to create. You can always take advantage of the POJO's Builder available for every case. E.g.

If you want to create an _invoice_:

```java
InvoiceInputModel pojo = InvoiceInputModel.Builder.anInvoiceInputModel().build();
```

If you want to create a _credit note_:

```java
CreditNoteInputModel pojo = CreditNoteInputModel.Builder.aCreditNoteInputModel()
```

You will be familiarized with every single POJO as you go through the docs.
