---
id: bvs_getstatus
title: BillValidService:getStatus
---

Use for:

- Check the authenticity of a file.

## Check status

```java
File file = new File("../folder/F001-00005954.xml");

ServiceConfig config = new ServiceConfig.Builder()
    .url("https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService")
    .username(USERNAME)
    .passwod(PASSWORD)
    .build();

StatusResponse status = BillValidServiceManager.getStatus(file, config);
```
