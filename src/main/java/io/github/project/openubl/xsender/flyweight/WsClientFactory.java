package io.github.project.openubl.xsender.flyweight;

import io.github.project.openubl.xsender.cxf.WsClientAuth;

public interface WsClientFactory {

    <T> T getInstance(Class<T> a, WsClientAuth auth);

}
