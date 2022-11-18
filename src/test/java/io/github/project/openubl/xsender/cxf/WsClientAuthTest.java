package io.github.project.openubl.xsender.cxf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WsClientAuthTest {

    @Test
    public void givenSameData_shouldBeEquals() {
        WsClientAuth client1 = WsClientAuth.builder()
                .username("username")
                .password("password")
                .address("address")
                .build();
        WsClientAuth client2 = WsClientAuth.builder()
                .username("username")
                .password("password")
                .address("address")
                .build();

        assertEquals(client1, client2);
    }

    @Test
    public void givenDifferentData_shouldBeEquals() {
        WsClientAuth client1 = WsClientAuth.builder()
                .username("username")
                .password("password")
                .address("address1")
                .build();
        WsClientAuth client2 =  WsClientAuth.builder()
                .username("username")
                .password("password")
                .address("address2")
                .build();

        assertNotEquals(client1, client2);
    }
}
