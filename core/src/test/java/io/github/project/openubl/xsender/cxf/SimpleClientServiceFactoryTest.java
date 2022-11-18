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
package io.github.project.openubl.xsender.cxf;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import service.sunat.gob.pe.billservice.BillService;

public class SimpleClientServiceFactoryTest {

    @Test
    public void createsProxy() {
        // Given
        WsClientAuth auth = WsClientAuth
            .builder()
            .username("myUsername")
            .password("myPassword")
            .address("myAddress")
            .build();

        WsClientConfig client = WsClientConfig.builder().build();

        // Then
        ProxyClientServiceFactory proxyClientServiceFactory = new SimpleClientServiceFactory();
        BillService proxy = proxyClientServiceFactory.create(BillService.class, auth, client);

        assertNotNull(proxy);
    }
}
