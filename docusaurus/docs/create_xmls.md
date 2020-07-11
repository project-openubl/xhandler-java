---
id: create_xmls
title: Create XMLs
---

For creating XML files you need to interact with the set of REST endpoints _XBuilder Server_ exposes to you. For more information about the REST API documentation visit [OpenAPI](./openapi)

For executing the next set of examples you need to have _XBuilder Server_ running.

## Invoice

```shell script
curl -X POST \
-H "Content-Type: application/json" \
-d '{
  "serie": "F001",
  "numero": 1,
  "proveedor": {
    "ruc": "12345678912",
    "razonSocial": "Project OpenUBL"
  },
  "cliente": {
    "tipoDocumentoIdentidad": "RUC",
    "numeroDocumentoIdentidad": "12312312312",
    "nombre": "Nombre de mi cliente"
  },
  "detalle": [
    {
      "descripcion": "Nombre de producto o servicio",
      "precioUnitario": 1,
      "cantidad": 1,
      "tipoIgv": "GRAVADO_OPERACION_ONEROSA"
    }
  ]
}' \
http://localhost:8080/api/documents/invoice/create
```

## CreditNote

```shell script
curl -X POST \
-H "Content-Type: application/json" \
-d '{
  "serie": "F001",
  "numero": 1,
  "descripcionSustentoDeNota": "mi motivo",
  "serieNumeroComprobanteAfectado": "F001-1",
  "proveedor": {
    "ruc": "12345678912",
    "razonSocial": "Project OpenUBL"
  },
  "cliente": {
    "tipoDocumentoIdentidad": "RUC",
    "numeroDocumentoIdentidad": "12312312312",
    "nombre": "Nombre de mi cliente"
  },
  "detalle": [
    {
      "descripcion": "Nombre de producto o servicio",
      "precioUnitario": 1,
      "cantidad": 1
    }
  ]
}' \
http://localhost:8080/api/documents/credit-note/create
```

## DebitNote

```shell script
curl -X POST \
-H "Content-Type: application/json" \
-d '{
  "serie": "F001",
  "numero": 1,
  "descripcionSustentoDeNota": "mi motivo",
  "serieNumeroComprobanteAfectado": "F001-1",
  "proveedor": {
    "ruc": "12345678912",
    "razonSocial": "Project OpenUBL"
  },
  "cliente": {
    "tipoDocumentoIdentidad": "RUC",
    "numeroDocumentoIdentidad": "12312312312",
    "nombre": "Nombre de mi cliente"
  },
  "detalle": [
    {
      "descripcion": "Nombre de producto o servicio",
      "precioUnitario": 1,
      "cantidad": 1
    }
  ]
}' \
http://localhost:8080/api/documents/debit-note/create
```

## VoidedDocument

```shell script
curl -X POST \
-H "Content-Type: application/json" \
-d '{
  "numero": 1,
  "descripcionSustento": "mi motivo de baja",
  "proveedor": {
    "ruc": "12345678912",
    "razonSocial": "Project OpenUBL"
  },
  "comprobante": {
    "serieNumero": "F001-1",
    "tipoComprobante": "FACTURA",
    "fechaEmision": 1585398109198
  }
}' \
http://localhost:8080/api/documents/voided-document/create
```

## SummaryDocument

```shell script
curl -X POST \
-H "Content-Type: application/json" \
-d '{
  "numero": 1,
  "fechaEmisionDeComprobantesAsociados": 1585398109198,
  "proveedor": {
    "ruc": "12345678912",
    "razonSocial": "Project OpenUBL"
  },
  "detalle": [{
    "tipoOperacion": "ADICIONAR",
    "comprobante": {
    	"tipo": "FACTURA",
    	"serieNumero": "F001-1",
    	"cliente": {
    		"tipoDocumentoIdentidad": "RUC",
    		"numeroDocumentoIdentidad": "12121212121",
    		"nombre": "nombre de mi cliente"
    	},
    	"valorVenta": {
    		"importeTotal": 100,
    		"gravado": 100,
    		"exonerado": 0,
    		"inafecto": "0",
    		"gratuito": "0"
    	},

    	"impuestos": {
    		"igv": 22,
    		"icb": 0
    	}
    }
  }, {
    "tipoOperacion": "ADICIONAR",
    "comprobante": {
    	"tipo": "NOTA_CREDITO",
    	"serieNumero": "BC01-1",
    	"cliente": {
    		"tipoDocumentoIdentidad": "DNI",
    		"numeroDocumentoIdentidad": "12121212",
    		"nombre": "nombre de mi cliente"
    	},
    	"valorVenta": {
    		"importeTotal": 100,
    		"gravado": 100,
    		"exonerado": 0,
    		"inafecto": "0",
    		"gratuito": "0"
    	},

    	"impuestos": {
    		"igv": 22,
    		"icb": 0
    	}
    },
    "comprobanteAfectado": {
    	"tipo": "BOLETA",
    	"serieNumero": "B001-1"
    }
  }]
}' \
http://localhost:8080/api/documents/summary-document/create
```
