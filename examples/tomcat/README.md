## Deploy

- Compile the application

```shell
mvn clean package
```

- Use Cargo to download and deploy it to Tomcat

```shell
mvn cargo:run
```

- Open http://localhost:8080/tomcat-xbuilder-xsender/

