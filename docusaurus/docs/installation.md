---
id: installation
title: Installation
---

## Docker

The easiest way of starting _XBuilder Server_ is using Docker.

```shell script
docker run -p 8080:8080 docker.io/projectopenubl/xbuilder-server
```

Then open [http://localhost:8080](http://localhost:8080) and verify that the server is running.

## JVM

_XBuilder Server_ can be executed as a common _.jar_ application.

- Download [xbuilder-server-myVersion-jvm.tgz](https://github.com/project-openubl/xbuilder-server/releases) and uncompress it.
- Open a terminal and move to the folder where you uncompressed the downloaded file. Execute:

```shell script
java -jar xbuilder-*-runner.jar
```

Then open [http://localhost:8080](http://localhost:8080) and verify that the server is running.

## Linux

_XBuilder Server_ distributes a native Linux executable.

- Download [xbuilder-server-myVersion-linux](https://github.com/project-openubl/xbuilder-server/releases).
- Open a terminal and move to the folder where you downloaded the file. Execute:

```shell script
./xbuilder-server-*
```

## Windows

_XBuilder Server_ distributes a native Windows executable.

- Download [xbuilder-server-myVersion-win64.exe](https://github.com/project-openubl/xbuilder-server/releases).
- Open a terminal and move to the folder where you downloaded the file. Execute:

```shell script
start xbuilder-server-*
```

## Java Application Server

_XBuilder Server_ distributes a `.war` file that can be deployed in any Java Application Server like Wildfly, Glasfish, Weblogic, etc..

- Download [xbuilder-server-myVersion.war](https://github.com/project-openubl/xbuilder-server/releases).
- Deploy the war file depending on your Application Server.
