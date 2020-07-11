---
id: create_xml
title: Create XML
---

Before creating an XML you need to decide which type of XML you will create; there are multiple types of XML files like:

- Invoices
- Credit notes
- Debit notes, etc.

Once you decided which type of document you want to create:

- Create your input POJO
- Send your POJO to `DocumentManager`

```java
// Create Invoice
DocumentWrapper<InvoiceOutputModel> result = DocumentManager.createXML(input, config, systemClock);

InvoiceOutputModel output = result.getOutput(); // XML Var values
String xml = result.getXml(); // XML content
```

## A fully working example

A fully working example of how to create an XML file is located in:

- [Example](./example)
