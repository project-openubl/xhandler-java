---
id: bs_getstatus
title: BillService:getStatus
---

Use for check _Tickets_:

## Check _ticket_

```java
ServiceConfig config = new ServiceConfig.Builder()
    .url("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
    .username("12345678959MODDATOS")
    .password("MODDATOS")
    .build();

BillServiceModel result = BillServiceManager.getStatus("miTicket", config);
```
