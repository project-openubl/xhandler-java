---
id: sign_xmls
title: Sign XMLs
---

There are 2 ways of signing your XMLs:

- Create your XML files using `X-OPENBUL-PRIVATEKEY` and `X-OPENUBL-CERTIFICATEKEY` headers.
- Configure the server and use a single certificate to sign all XMLs,

## Method 1: Sign using Headers

You need to provide 3 things:

- **Body** - The body of the request which contains info about the document you are about to create.
- **X-OPENBUL-PRIVATEKEY** - The header which contains the PEM encoded version of your private key.
- **X-OPENUBL-CERTIFICATEKEY** - The header which contains the PEM encoded version of your certificate.

```shell script
curl -X POST \
-H "Content-Type: application/json" \
-H "X-OPENBUL-PRIVATEKEY: MIIEowIBAAKCAQEArhO3H48lGkRNcPNhA6uTd804NnMxBkXKKTgR8DldX8vTmrf0JqNGMLxUlqSG1KlRelHQXvIz7GWO0NgE0DZ9eMEULS7S8YMuj6RZFCudDb/aasxHyCvjVfdKJUF4BIPPKN2dvjFBAQz4fI/3/PceptIqzwzl+8SryXEbJgAUmjaS2POE65RePRIINOV1Vi7lwvLzH0Zl1sr+oytOnXAI1YRlKZhgcS5v5XeX/qfRpbIQdqxcloVAQX/voN8QsLT6chZr/gEZbUnDs2HD286/Xzg27Rw8Bwy7HIbhhKYPK2TsFanpMhsTVtC3gxp6umLb3Fuala7RBC76nDZC9A+95QIDAQABAoIBABhHrbIcMCuivT504+I0K0R5fk6x8HOUhmcLaA0eozR6ZJBe+hHtkhu4GQBOAHRnDXNHOA4WMEHXxHzCtKEqCIQwQhUvQ8Ll7jegz7/teWFykg91YMm9vV6/ODtMD2Zp0Bo+FwNxMUTpPzt4hTlmaoMQK2JnxShBvUhCm2vIdRcxLHV63HjRWqHu98vKYxQ5ByQX3nVBP757zRI2rhC5g0yzQucGj2GMeD3t8W/NozNaUx9qXq2YaqhIYfhbzKZH41ZeIpE0Au7aNS4WBTpWkO1patCpSZHhTV9RIbBCG7al0ukLs3FfbWoHCAJAHUyuEvG4htSb0WqudlJn/rPNdP0CgYEA4SK8NgON4wvdi42dr43NdcOVbWes4HM4M8f1pi7W9RSracuAXj7oeyirKPnUMJchRNOlF/aTghbgtbgAYBFxYYfFbc12BURiAgo6firu6ILD7696V/uxiQPSg/chVrBkN1rYYf1sTgcJB9N7uuiBQZAh8NJWJNvviPVxNfFhoO8CgYEAxfEMCnEBiiOiKi72eclGGAAQr/JAdoaCXZPi1lmbAkWULtyqoo3MzyuyJ3GDwb1j8e2JqPEvqAW8w993Z5vqk/N9MA7rlSE6UPxTHs8ZKNWcdci0rReurG+evrGRRJmKuvqK0/7Nqr/f039VuRqvgtWxJeFoBNZVpwGG/LeCJmsCgYAVcjyhnJcQkNnK6HOj/Isc88OxR1YFj5REAoFZEk8xy4VEr7kLwUxeJxKe9aWL92mY59xrOvb0Rn+jb+LBRAgb9VYOTqs2dzwq25SU3jwh9Ar8MyghZ32TAsU0Av+vBWCWkVXZh82gZTUsBK5dsLZXa4aALVk9a6IW1uKw88yMCwKBgFk3e2jdZIdB5l7DCh78ZFZ++QaE1x9VIz9QX8ajXqWYfODeXx6jcTPTixoSJQPW/ExX91spUoSWCW3ztBsEAKgs8DkQEIkIEAPepwxU5g8ssLe5/g2ihf181f03hbV4yznZoWdKCqMyloz6cMXczEzZSl47iancfYCnxJL1l3j/AoGBAIQDUua/Ia2LLJE24kamiLmdtECHsXg/Wrp++YaGc2btHblAN5TNQfy3S4yvQIOzaVp7AQMXq/AUdua1YcLS1Op/CsocgVMzpckZ7FVS8BFuQnQx8ltnAcqbnCo6UzUdOPKNRw2EDyk9yK83wEtvkvlHOVdRsOlYN5ZSrkq1X92A" \
-H "X-OPENUBL-CERTIFICATEKEY: MIIE+DCCA+CgAwIBAgIJALURz7AYmJ5+MA0GCSqGSIb3DQEBBQUAMIIBDTEbMBkGCgmSJomT8ixkARkWC0xMQU1BLlBFIFNBMQswCQYDVQQGEwJQRTENMAsGA1UECAwETElNQTENMAsGA1UEBwwETElNQTEYMBYGA1UECgwPVFUgRU1QUkVTQSBTLkEuMUUwQwYDVQQLDDxETkkgOTk5OTk5OSBSVUMgMTA0Njc3OTM1NDkgLSBDRVJUSUZJQ0FETyBQQVJBIERFTU9TVFJBQ0nDk04xRDBCBgNVBAMMO05PTUJSRSBSRVBSRVNFTlRBTlRFIExFR0FMIC0gQ0VSVElGSUNBRE8gUEFSQSBERU1PU1RSQUNJw5NOMRwwGgYJKoZIhvcNAQkBFg1kZW1vQGxsYW1hLnBlMB4XDTE5MTEwODEyNTY1MFoXDTIxMTEwNzEyNTY1MFowggENMRswGQYKCZImiZPyLGQBGRYLTExBTUEuUEUgU0ExCzAJBgNVBAYTAlBFMQ0wCwYDVQQIDARMSU1BMQ0wCwYDVQQHDARMSU1BMRgwFgYDVQQKDA9UVSBFTVBSRVNBIFMuQS4xRTBDBgNVBAsMPEROSSA5OTk5OTk5IFJVQyAxMDQ2Nzc5MzU0OSAtIENFUlRJRklDQURPIFBBUkEgREVNT1NUUkFDScOTTjFEMEIGA1UEAww7Tk9NQlJFIFJFUFJFU0VOVEFOVEUgTEVHQUwgLSBDRVJUSUZJQ0FETyBQQVJBIERFTU9TVFJBQ0nDk04xHDAaBgkqhkiG9w0BCQEWDWRlbW9AbGxhbWEucGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCuE7cfjyUaRE1w82EDq5N3zTg2czEGRcopOBHwOV1fy9Oat/Qmo0YwvFSWpIbUqVF6UdBe8jPsZY7Q2ATQNn14wRQtLtLxgy6PpFkUK50Nv9pqzEfIK+NV90olQXgEg88o3Z2+MUEBDPh8j/f89x6m0irPDOX7xKvJcRsmABSaNpLY84TrlF49Egg05XVWLuXC8vMfRmXWyv6jK06dcAjVhGUpmGBxLm/ld5f+p9GlshB2rFyWhUBBf++g3xCwtPpyFmv+ARltScOzYcPbzr9fODbtHDwHDLschuGEpg8rZOwVqekyGxNW0LeDGnq6YtvcW5qVrtEELvqcNkL0D73lAgMBAAGjVzBVMB0GA1UdDgQWBBTe18LHVKkeRrWs3Bxp1ikK50l96jAfBgNVHSMEGDAWgBTe18LHVKkeRrWs3Bxp1ikK50l96jATBgNVHSUEDDAKBggrBgEFBQcDATANBgkqhkiG9w0BAQUFAAOCAQEASBWcP4AiqUUZSG2/Z3RU3BgvOVV3if8xYAaZT99n5PsvyBiZ3Gh6VpAW9ezoe25ZNSqGMmGfq+R4mEuiqK4h6jDJp0fN47IwRhWjttB9dwpxIDEkWW7zPdueGx+BY8EuyfNDWR/C7GPfu6azSHiapzeKC57AAZ8xo8kDdhXxDy8hTqNGolkqnc6QutW8cYPeonqyhi2THN163lZ3Cx5OV8vGFQ3ou2msF0klY9qXopI9i8wQjEeOG6bCvVxdID9ZjTbuGbO9pAN9hH7hZ41XEG/CspSWbFf1/Wbnlfusne9v9NgRj0MM+LAHM7AO5/7j1XwRq4x+U9TSVPgpoU9l5Q==" \
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

## Method 2: Sign configuring the server

You need to configure the properties `openubl.server.keystore.location` and `openubl.server.keystore.password` so every single XML is signed.

> Even after using this method you are always able to override the certificate to sign an XML using the previous method.

| variable                         | default value | description                                                                                            |
| -------------------------------- | ------------- | ------------------------------------------------------------------------------------------------------ |
| openubl.server.keystore.location | _null_        | (Optional) The location of the certificate you want to use if you'd like to sign all your certificates |
| openubl.server.keystore.password | _null_        | (Optional) The password of the certificate you want to use if you'd like to sign all your certificates |

To know more about how to override properties in the server read [Configuration](./configuration)
