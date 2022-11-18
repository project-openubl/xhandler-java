package io.github.project.openubl.xsender.cxf;

import org.junit.jupiter.api.Test;
import service.sunat.gob.pe.billservice.BillService;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleClientServiceFactoryTest {

    @Test
    public void createsProxy() {
        // Given
        WsClientAuth auth = WsClientAuth.builder()
                .username("myUsername")
                .password("myPassword")
                .address("myAddress")
                .build();

        WsClientConfig client = WsClientConfig.builder()
                .build();

        // Then
        ProxyClientServiceFactory proxyClientServiceFactory = new SimpleClientServiceFactory();
        BillService proxy = proxyClientServiceFactory.create(BillService.class, auth, client);

        assertNotNull(proxy);
    }

}
