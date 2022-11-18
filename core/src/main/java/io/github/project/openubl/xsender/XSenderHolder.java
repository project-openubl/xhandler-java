/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xsender;

import io.github.project.openubl.xsender.cxf.ProxyClientServiceFactory;
import io.github.project.openubl.xsender.cxf.SimpleClientServiceFactory;
import io.github.project.openubl.xsender.cxf.WsClientConfig;
import io.github.project.openubl.xsender.flyweight.SimpleWsClientFactory;
import io.github.project.openubl.xsender.flyweight.WsClientFactory;
import lombok.Synchronized;

public class XSenderHolder {

    private static XSenderHolder instance;

    private final XSender xSender;

    private XSenderHolder() {
        ProxyClientServiceFactory proxyClientServiceFactory = new SimpleClientServiceFactory();
        WsClientConfig wsClientConfig = WsClientConfig
            .builder()
            .connectionTimeout(15_000L)
            .receiveTimeout(10_000L)
            .build();

        WsClientFactory wsClientFactory = new SimpleWsClientFactory(proxyClientServiceFactory, wsClientConfig);
        this.xSender = new XSender(wsClientFactory);
    }

    @Synchronized
    public static XSenderHolder getInstance() {
        instance = new XSenderHolder();
        return instance;
    }

    public XSender getxSender() {
        return xSender;
    }
}
