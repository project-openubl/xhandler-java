package io.github.project.openubl.xsender.camel;

import io.github.project.openubl.xsender.Constants;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.models.rest.ResponseAccessTokenSuccessDto;
import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.main.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiCredentialsTest {

    CompanyURLs companyURLs = CompanyURLs
            .builder()
            .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")
            .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
            .build();

    CompanyCredentials credentials = CompanyCredentials
            .builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
            .build();

    private static CamelContext camelContext;

    @BeforeAll
    public static void beforeEach() {
        Main mainCamel = StandaloneCamel.getInstance().getMainCamel();
        if (!mainCamel.isStarted()) {
            mainCamel.start();
        }
        camelContext = mainCamel.getCamelContext();
    }

    @AfterAll
    public static void afterEach() {
        StandaloneCamel.getInstance().getMainCamel().stop();
    }

    @Test
    public void nonExpiredTokenShouldNotBeRefreshed() throws Exception {
        ResponseAccessTokenSuccessDto prevToken = ResponseAccessTokenSuccessDto.builder()
                .access_token("myInitialToken")
                .expires_in(60)
                .created_in(ZonedDateTime.now())
                .build();

        String clientId = "myClientId";

        Map<String, Object> headers = Map.of(
                HttpConstants.HTTP_URI, "https://api-cpe.sunat.gob.pe",
                HttpConstants.HTTP_PATH, "/v1/clientessol/" + clientId + "/oauth2/token/"
        );
        Object body = List.of(prevToken, Map.of(
                "grant_type", "password",
                "scope", "https://api-cpe.sunat.gob.pe",
                "client_id", clientId,
                "client_secret", "mySecret",
                "username", "myUsername",
                "password", "myPassword"
        ));

        ResponseAccessTokenSuccessDto newToken = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_CREDENTIALS_API_URI, body, headers, ResponseAccessTokenSuccessDto.class);

        assertNotNull(newToken);
        assertEquals(prevToken, newToken);
    }

    // TODO Mock SUNAT response and make this test work
    @Disabled
    @Test
    public void expiredTokenShouldBeRefreshed() throws Exception {
        ResponseAccessTokenSuccessDto prevToken = ResponseAccessTokenSuccessDto.builder()
                .access_token("myInitialToken")
                .expires_in(60)
                .created_in(ZonedDateTime.now().minusDays(1))
                .build();

        String clientId = "myClientId";

        Map<String, Object> headers = Map.of(
                HttpConstants.HTTP_URI, "https://api-cpe.sunat.gob.pe",
                HttpConstants.HTTP_PATH, "/v1/clientessol/" + clientId + "/oauth2/token/"
        );
        Object body = List.of(prevToken, Map.of(
                "grant_type", "password",
                "scope", "https://api-cpe.sunat.gob.pe",
                "client_id", clientId,
                "client_secret", "mySecret",
                "username", "myUsername",
                "password", "myPassword"
        ));

        ResponseAccessTokenSuccessDto newToken = camelContext
                .createProducerTemplate()
                .requestBodyAndHeaders(Constants.XSENDER_CREDENTIALS_API_URI, body, headers, ResponseAccessTokenSuccessDto.class);

        assertNotNull(newToken);
        assertEquals(prevToken, newToken);
    }
}
