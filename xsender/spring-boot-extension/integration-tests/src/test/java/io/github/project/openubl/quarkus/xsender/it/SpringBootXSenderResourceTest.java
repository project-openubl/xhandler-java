package io.github.project.openubl.quarkus.xsender.it;

import io.github.project.openubl.springboot.xsender.it.Application;
import io.github.project.openubl.xsender.models.Status;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SpringBootXSenderResourceTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void sendInvoice() {
        given()
                .when()
                .post("http://localhost:" + port + "/springboot-xsender/bill-service/send-invoice")
                .then()
                .statusCode(200)
                .body(is(Status.ACEPTADO.toString()));
    }

    @Test
    public void sendVoidedDocument() {
        given()
                .when()
                .post("http://localhost:" + port + "/springboot-xsender/bill-service/send-voided-document")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }
}
