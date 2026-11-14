# Contribuir a XHandler Java

XHandler Java es una capa delgada sobre el
[núcleo xhandler](https://github.com/project-openubl/xhandler-rust). Crear, firmar, enrutar,
comprimir, armar las peticiones a la SUNAT y leer sus respuestas ocurre en Rust; Java solo envía las
peticiones HTTP e integra los frameworks. Los cambios a esa lógica van en xhandler-rust.

## Entorno

Java 25 y Maven. `make sync` necesita además un checkout de xhandler-rust junto a este (revisa
[su CONTRIBUTING.md](https://github.com/project-openubl/xhandler-rust/blob/main/CONTRIBUTING.md)).
También puedes abrir el repositorio en el devcontainer (`.devcontainer/`).

```shell
mvn verify -Pexamples                                      # todos los módulos y los ejemplos
mvn verify -pl xbuilder/core -am                           # un módulo y sus dependencias
mvn test -pl xsender/core -Dtest=SunatClientSoapTest       # una clase de tests
```

Pruebas de integración (no están en el reactor por defecto):

```shell
mvn install -DskipTests
mvn verify -f xbuilder/quarkus-extension/integration-tests/              # JVM
mvn verify -f xbuilder/quarkus-extension/integration-tests/ -Pnative-image  # nativo (GraalVM o contenedor)
mvn verify -f xsender/spring-boot-extension/integration-tests/
```

Contra la SUNAT beta (opcionales, necesitan red):

```shell
XHANDLER_E2E=true mvn test -pl xbuilder/core -Dtest=E2ETest                # crea, firma y envía una factura
mvn test -pl xsender/core -Dtest=SunatBetaTest -Dxsender.sunat-beta=true
```

## Estructura

| Ruta | |
|---|---|
| `xhandler-core/` | Ejecuta `xhandler.wasm` con [Chicory](https://chicory.dev): el plugin de Maven lo compila a bytecode (`XhandlerModule`); `Xhandler.invoke`, `XhandlerException`, `ErrorKind`, `Json` y la configuración de GraalVM (`META-INF/native-image`) |
| `xbuilder/core/` | `XBuilder`, `XSigner`, `SigningKey`; `models/` es generado |
| `xsender/core/` | `FileSender` y `SunatClient` (`java.net.http`): timeouts, reintentos, caché de tokens OAuth de las guías |
| `*/quarkus-extension/` | Extensiones de Quarkus (`runtime`, `deployment`, `integration-tests`) |
| `xsender/spring-boot-extension/`, `xsender/cdi/` | Starter de Spring Boot y beans CDI |
| `examples/` | Ejemplos (perfil `examples`) |
| `testdata/conformance/` | El corpus de conformidad, copiado desde xhandler-rust |

## Cómo funciona

El núcleo expone un solo punto de entrada JSON: la petición es `{"op", "input"}` y la respuesta
`{"ok": ...}` o `{"error": {"kind", "message"}}`. `Xhandler` mantiene un pool de instancias; una
instancia atiende una llamada a la vez y se descarta si falla (un panic en el núcleo).

`SunatClient` pide al núcleo la petición HTTP de cada llamada (op `request`), la envía y le entrega
la respuesta (op `parse`). Se encarga de lo que necesita IO: timeouts (30s/120s), un reintento si la
conexión falla, reintentos tras respuestas `rate_limited` (1s y 2s) y los tokens de las guías
(reutilizados hasta un minuto antes de expirar, un pedido a la vez, reintento con un token nuevo
ante HTTP 401).

## Archivos generados

Vienen de xhandler-rust y no se editan a mano:

- `xhandler-core/src/main/wasm/xhandler.wasm` y su `VERSION`
- `xbuilder/core/src/main/java/io/github/project/openubl/xbuilder/models/`
- `testdata/conformance/`

```shell
make sync RUST_REPO=../xhandler-rust   # corre make -C xhandler-wasm dist allá y los copia
```

Un cambio de modelos o de comportamiento empieza en xhandler-rust; luego `make sync` aquí, corrige
lo que señalen el compilador o los tests y abre un PR con los archivos copiados.

## Tests

- `XhandlerTest`: cada caso del corpus por el punto de entrada JSON.
- `XBuilderTest`, `XSignerTest`: los casos del corpus a través de los modelos. Jackson rechaza
  propiedades desconocidas, así que un modelo desactualizado falla aquí.
- `SunatClientSoapTest`, `SunatClientGreTest`, `FileSenderTest`: un `FakeSunat` (`HttpServer` del
  JDK) responde como la SUNAT: CDR, faults, rate limiting, tokens de guías.

## CI

- En cada PR: `JDK 25 JVM Tests` en Linux, macOS (arm64) y Windows; `quarkus-extension` (build
  nativo por extensión: la única verificación de la configuración de GraalVM); la versión más
  antigua soportada de Quarkus (JVM) y de Spring Boot.
- Checks requeridos: `JDK 25 JVM Tests`, `quarkus-extension (xbuilder)`,
  `quarkus-extension (xsender)` y `xsender-springboot-extension`. Los PRs de Dependabot se
  aprueban y fusionan solos cuando pasan.
- Las versiones se publican con el workflow `Release` (manual, JReleaser).

## Convenciones

- Commits y títulos de PR con [Conventional Commits](https://www.conventionalcommits.org/)
  (`feat:`, `fix:`, `docs:`, `ci:`, `chore:`...); los cambios incompatibles llevan `!`.
- Los nombres siguen a las otras librerías (`FileSender`, `SunatClient`, `SigningKey`...):
  mantenlos alineados con Go y Python.

## Licencia

Al contribuir aceptas que tus contribuciones se distribuyan bajo la
[Apache License, Version 2.0](LICENSE).
