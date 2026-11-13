package io.github.project.openubl.xsender.sunat;

import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SunatClientGreTest {

    private static final String TOKEN_PATH = "/v1/clientessol/my-client-id/oauth2/token/";
    private static final String SEND_PATH = "/v1/contribuyente/gem/comprobantes/20123456789-09-T001-1";
    private static final String TICKET_PATH = "/v1/contribuyente/gem/comprobantes/envios/my-ticket";

    private final FakeSunat sunat = new FakeSunat();
    private final SunatClient client = new SunatClient(SunatClient.defaultHttpClient(), sunat.url(""));
    private final CompanyCredentials credentials = CompanyCredentials.builder()
            .username("20123456789MODDATOS")
            .password("MODDATOS")
            .clientId("my-client-id")
            .clientSecret("my-secret")
            .build();
    private final ZipFile zipFile = new ZipFile("abc".getBytes(StandardCharsets.UTF_8), "20123456789-09-T001-1.zip");

    @BeforeEach
    void respondToken() {
        sunat.respondJson(TOKEN_PATH, 200, """
                {"access_token": "my-token", "token_type": "JWT", "expires_in": 3600}""");
    }

    @AfterEach
    void stopSunat() {
        sunat.close();
    }

    private BillServiceDestination destination(BillServiceDestination.RestOperation operation) {
        return BillServiceDestination.builder().url(sunat.url("/v1/contribuyente/gem")).restOperation(operation).build();
    }

    @Test
    void send_returnsTheTicket() {
        sunat.respondJson(SEND_PATH, 200, """
                {"numTicket": "my-ticket", "fecRecepcion": "2024-01-10T10:00:00"}""");

        SunatResponse response = client.sendFile(destination(BillServiceDestination.RestOperation.SEND_DOCUMENT), zipFile, credentials);

        assertEquals("my-ticket", response.getSunat().getTicket());
    }

    @Test
    void send_postsTheZipWithTheAccessToken() {
        sunat.respondJson(SEND_PATH, 200, """
                {"numTicket": "my-ticket"}""");

        client.sendFile(destination(BillServiceDestination.RestOperation.SEND_DOCUMENT), zipFile, credentials);

        FakeSunat.Request token = sunat.lastRequest(TOKEN_PATH);
        assertTrue(token.body().contains("grant_type=password"));
        assertTrue(token.body().contains("scope=https%3A%2F%2Fapi-cpe.sunat.gob.pe"));
        assertTrue(token.body().contains("username=20123456789MODDATOS"));
        assertTrue(token.body().contains("client_secret=my-secret"));

        FakeSunat.Request send = sunat.lastRequest(SEND_PATH);
        assertEquals("Bearer my-token", send.header("Authorization"));
        assertEquals("""
                {"archivo":{"nomArchivo":"20123456789-09-T001-1.zip","arcGreZip":"YWJj",\
                "hashZip":"ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"}}""", send.body());
    }

    @Test
    void accessToken_isReused() {
        sunat.respondJson(SEND_PATH, 200, """
                {"numTicket": "my-ticket"}""");
        BillServiceDestination destination = destination(BillServiceDestination.RestOperation.SEND_DOCUMENT);

        client.sendFile(destination, zipFile, credentials);
        client.sendFile(destination, zipFile, credentials);

        assertEquals(1, sunat.requests(TOKEN_PATH).size());
    }

    @Test
    void accessToken_isRequestedAgainOnceExpired() {
        sunat.respondJson(TOKEN_PATH, 200, """
                {"access_token": "my-token", "expires_in": 60}""");
        sunat.respondJson(SEND_PATH, 200, """
                {"numTicket": "my-ticket"}""");
        BillServiceDestination destination = destination(BillServiceDestination.RestOperation.SEND_DOCUMENT);

        client.sendFile(destination, zipFile, credentials);
        client.sendFile(destination, zipFile, credentials);

        assertEquals(2, sunat.requests(TOKEN_PATH).size());
    }

    @Test
    void rejectedAccessToken_isReplacedAndTheRequestRetried() {
        sunat.respondJson(SEND_PATH, 200, """
                {"numTicket": "my-ticket"}""");
        BillServiceDestination destination = destination(BillServiceDestination.RestOperation.SEND_DOCUMENT);
        client.sendFile(destination, zipFile, credentials);

        sunat.respondSequence(SEND_PATH,
                401, "application/json", """
                        {"cod": "401", "msg": "No autorizado"}""",
                200, "application/json", """
                        {"numTicket": "my-second-ticket"}""");
        SunatResponse response = client.sendFile(destination, zipFile, credentials);

        assertEquals("my-second-ticket", response.getSunat().getTicket());
        assertEquals(2, sunat.requests(TOKEN_PATH).size());
    }

    @Test
    void tokenInTheCredentials_isUsedAsIs() {
        sunat.respondJson(SEND_PATH, 200, """
                {"numTicket": "my-ticket"}""");
        CompanyCredentials withToken = CompanyCredentials.builder().token("given-token").build();

        client.sendFile(destination(BillServiceDestination.RestOperation.SEND_DOCUMENT), zipFile, withToken);

        assertEquals("Bearer given-token", sunat.lastRequest(SEND_PATH).header("Authorization"));
        assertTrue(sunat.requests(TOKEN_PATH).isEmpty());
    }

    @Test
    void serverError_throws() {
        sunat.respondJson(SEND_PATH, 503, """
                {"cod": "503", "msg": "Service Unavailable"}""");

        assertThrows(XSenderException.class,
                () -> client.sendFile(destination(BillServiceDestination.RestOperation.SEND_DOCUMENT), zipFile, credentials));
    }

    @Test
    void send_returnsSunatErrors() {
        sunat.respondJson(SEND_PATH, 422, """
                {"cod": "422", "msg": "Unprocessable Entity",
                 "errors": [{"cod": "1033", "msg": "El comprobante fue registrado previamente"}]}""");

        SunatResponse response = client.sendFile(destination(BillServiceDestination.RestOperation.SEND_DOCUMENT), zipFile, credentials);

        assertEquals(422, response.getMetadata().getResponseCode());
        assertEquals("Unprocessable Entity", response.getMetadata().getDescription());
        assertEquals(List.of("1033 - El comprobante fue registrado previamente"), response.getMetadata().getNotes());
    }

    @Test
    void send_withoutClientCredentials_throws() {
        CompanyCredentials withoutClient = CompanyCredentials.builder().username("u").password("p").build();

        assertThrows(XSenderException.class,
                () -> client.sendFile(destination(BillServiceDestination.RestOperation.SEND_DOCUMENT), zipFile, withoutClient));
    }

    @Test
    void verifyTicket_returnsTheCdr() {
        sunat.respondJson(TICKET_PATH, 200, """
                {"codRespuesta": "0", "indCdrGenerado": "1", "arcCdr": "%s"}""".formatted(cdrBase64()));

        SunatResponse response = client.verifyTicket(destination(BillServiceDestination.RestOperation.VERIFY_TICKET), "my-ticket", credentials);

        assertEquals(Status.ACEPTADO, response.getStatus());
        assertEquals(0, response.getMetadata().getResponseCode());
        assertNotNull(response.getSunat().getCdr());
        assertEquals("Bearer my-token", sunat.lastRequest(TICKET_PATH).header("Authorization"));
    }

    @Test
    void verifyTicket_inProgress() {
        sunat.respondJson(TICKET_PATH, 200, """
                {"codRespuesta": "98"}""");

        SunatResponse response = client.verifyTicket(destination(BillServiceDestination.RestOperation.VERIFY_TICKET), "my-ticket", credentials);

        assertEquals(Status.EN_PROCESO, response.getStatus());
    }

    @Test
    void verifyTicket_rejected() {
        sunat.respondJson(TICKET_PATH, 200, """
                {"codRespuesta": "99", "indCdrGenerado": "0",
                 "error": {"numError": "2335", "desError": "El documento electrónico ingresado ha sido alterado"}}""");

        SunatResponse response = client.verifyTicket(destination(BillServiceDestination.RestOperation.VERIFY_TICKET), "my-ticket", credentials);

        assertEquals(Status.RECHAZADO, response.getStatus());
        assertEquals(2335, response.getMetadata().getResponseCode());
        assertEquals("El documento electrónico ingresado ha sido alterado", response.getMetadata().getDescription());
    }

    /** The zipped CDR SUNAT returned in a SOAP fixture */
    private static String cdrBase64() {
        Matcher matcher = Pattern.compile("<applicationResponse>(.*)</applicationResponse>")
                .matcher(FakeSunat.resource("/sunat/bill_service_response_ok.xml"));
        assertTrue(matcher.find());
        return matcher.group(1);
    }
}
