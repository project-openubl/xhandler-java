# Contribuir a XHandler Java

## Requisitos

- Java 25 y Maven.
- Para los tests nativos de Quarkus: GraalVM o un runtime de contenedores.
- Para cambiar el núcleo: un checkout de
  [xhandler-rust](https://github.com/project-openubl/xhandler-rust) junto a este (revisa
  [su CONTRIBUTING.md](https://github.com/project-openubl/xhandler-rust/blob/main/CONTRIBUTING.md)).

O abre el repositorio en el devcontainer (`.devcontainer/`).

## Comandos

```shell
mvn verify -Pexamples                                  # todos los módulos y los ejemplos
mvn verify -pl xbuilder/core -am                       # un módulo y sus dependencias
mvn test -pl xsender/core -Dtest=SunatClientSoapTest   # una clase de tests
XHANDLER_E2E=true mvn test -pl xbuilder/core -Dtest=E2ETest   # crea, firma y envía una factura a la SUNAT beta
```

Las pruebas de integración de las extensiones no están en el reactor por defecto:

```shell
mvn install -DskipTests
mvn verify -f xbuilder/quarkus-extension/integration-tests/                 # JVM
mvn verify -f xbuilder/quarkus-extension/integration-tests/ -Pnative-image  # nativo
mvn verify -f xsender/spring-boot-extension/integration-tests/
```

## Hacer un cambio

- Crear, firmar, armar las peticiones a la SUNAT y leer sus respuestas ocurre en el núcleo de
  Rust (`xhandler-core/src/main/wasm/xhandler.wasm`). Esos cambios van en xhandler-rust; luego
  aquí:

  ```shell
  make sync RUST_REPO=../xhandler-rust
  ```

  `make sync` copia el núcleo, los modelos (`xbuilder/core/.../xbuilder/models/`) y el corpus de
  conformidad (`testdata/conformance/`). No los edites a mano.
- En Java está lo que necesita IO (`xsender/core`: cliente HTTP, reintentos, caché de tokens) y la
  integración con los frameworks (`*/quarkus-extension`, `xsender/spring-boot-extension`,
  `xsender/cdi`).
- Los nombres (`FileSender`, `SunatClient`, `SigningKey`...) se mantienen iguales en Go, Python y
  Java.

## Pull requests

- Título con [Conventional Commits](https://www.conventionalcommits.org/) (`feat:`, `fix:`,
  `docs:`...); los cambios incompatibles llevan `!`.
- Deben pasar `JDK 25 JVM Tests`, `quarkus-extension (xbuilder)`, `quarkus-extension (xsender)` y
  `xsender-springboot-extension`.

Al contribuir aceptas que tus contribuciones se distribuyan bajo la
[Apache License, Version 2.0](LICENSE).
