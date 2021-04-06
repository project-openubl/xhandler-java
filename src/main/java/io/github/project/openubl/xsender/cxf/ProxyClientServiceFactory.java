package io.github.project.openubl.xsender.cxf;

public interface ProxyClientServiceFactory {

    <T> T create(Class<T> tClass, WsClientAuth auth, WsClientConfig config);

}
