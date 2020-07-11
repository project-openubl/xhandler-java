---
id: configuration
title: Configuration
---

_XBuilder Server_ allows you to change the default configuration of the server. For configuring the server, _XBuilder Server_ uses variables.

## Variables

| variable                            | default value             | description                                                                                            |
| ----------------------------------- | ------------------------- | ------------------------------------------------------------------------------------------------------ |
| openubl.igv                         | 0.18                      | Value of the IGV tax                                                                                   |
| openubl.ivap                        | 0.04                      | Value of the IVAP tax                                                                                  |
| openubl.icb                         | 0.20                      | Value of the ICB tax                                                                                   |
| openubl.timezone                    | America/Lima              | Time zone in which automatic dates will be generated                                                   |
| openubl.server.keystore.location    | _null_                    | (Optional) The location of the certificate you want to use if you'd like to sign all your certificates |
| openubl.server.keystore.password    | _null_                    | (Optional) The password of the certificate you want to use if you'd like to sign all your certificates |
| openubl.defaults.moneda             | PEN                       | Default currency you want to use                                                                       |
| openubl.defaults.unidad-medida      | NIU                       | Default Unit of Measure                                                                                |
| openubl.defaults.tipo-igv           | GRAVADO_OPERACION_ONEROSA | Default type of _IGV_ taxes you want to apply                                                          |
| openubl.defaults.tipo-nota-credito  | ANULACION_DE_LA_OPERACION | Default type of _CreditNote_                                                                           |
| openubl.defaults.tipo-nota-debito   | AUMENTO_EN_EL_VALOR       | Default type of _DebitNote_                                                                            |
| openubl.defaults.regimen-percepcion | VENTA_INTERNA             | Default type of _Perception_                                                                           |
| openubl.defaults.regimen-retencion  | TASA_TRES                 | Default type of _Retention_                                                                            |

## Override variables

_XBuilder Server_ is based on Quarkus and you can take advantage of their system to override variables. For more information about how to override properties read [Quarkus Config](https://quarkus.io/guides/config#overriding-properties-at-runtime)

> You can override these runtime properties with the following mechanisms (in decreasing priority):
>
> 1. using system properties:
>
>    - for a runner jar: java -Dquarkus.datasource.password=youshallnotpass -jar target/myapp-runner.jar
>    - for a native executable: ./target/myapp-runner -Dquarkus.datasource.password=youshallnotpass
>
> 1. using environment variables:
>
>    - for a runner jar: export QUARKUS_DATASOURCE_PASSWORD=youshallnotpass ; java -jar target/myapp-runner.jar
>
>    - for a native executable: export QUARKUS_DATASOURCE_PASSWORD=youshallnotpass ; ./target/myapp-runner
>
> 1. using an environment file named .env placed in the current working directory containing the line QUARKUS_DATASOURCE_PASSWORD=youshallnotpass (for dev mode, this file can be placed in the root of the project, but it is advised to not check it in to version control)
>
> 1. using a configuration file placed in \$PWD/config/application.properties
>
>    - By placing an application.properties file inside a directory named config which resides in the directory where the application runs, any runtime properties defined in that file will override the default configuration. Furthermore any runtime properties added to this file that were not part of the original application.properties file will also be taken into account.
>
>    - This works in the same way for runner jar and the native executable
>
> **https://quarkus.io/guides/config#overriding-properties-at-runtime**

### Docker example

```shell script
docker run \
-e OPENUBL_ICB=0.9 \
projectopenubl/xbuilder-server
```

### JVM example

```shell script
java -Dopenubl.icb=0.9 -jar xbuilder-*-runner.jar
```

### Linux example

Using system properties:

```shell script
./xbuilder-server-* -Dopenubl.icb=0.9
```

Using environment variables:

```shell script
export OPENUBL_ICB=0.9 ; ./xbuilder-server-*
```
