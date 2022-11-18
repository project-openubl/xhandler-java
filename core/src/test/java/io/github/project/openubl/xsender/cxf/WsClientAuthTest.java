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

public class WsClientAuthTest {

    @Test
    public void givenSameData_shouldBeEquals() {
        WsClientAuth client1 = WsClientAuth
            .builder()
            .username("username")
            .password("password")
            .address("address")
            .build();
        WsClientAuth client2 = WsClientAuth
            .builder()
            .username("username")
            .password("password")
            .address("address")
            .build();

        assertEquals(client1, client2);
    }

    @Test
    public void givenDifferentData_shouldBeEquals() {
        WsClientAuth client1 = WsClientAuth
            .builder()
            .username("username")
            .password("password")
            .address("address1")
            .build();
        WsClientAuth client2 = WsClientAuth
            .builder()
            .username("username")
            .password("password")
            .address("address2")
            .build();

        assertNotEquals(client1, client2);
    }
}
