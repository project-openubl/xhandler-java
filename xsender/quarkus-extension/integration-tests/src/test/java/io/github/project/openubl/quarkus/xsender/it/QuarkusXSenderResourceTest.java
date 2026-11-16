package io.github.project.openubl.quarkus.xsender.it;

import io.github.project.openubl.xsender.models.Status;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@WithTestResource(FakeSunatResource.class)
@QuarkusTest
public class QuarkusXSenderResourceTest {

    @Test
    public void sendInvoice() {
        given()
                .when()
                .post("/quarkus-xsender/bill-service/send-invoice")
                .then()
                .statusCode(200)
                .body(is(Status.ACEPTADO.toString()));
    }

    @Test
    public void sendVoidedDocument() {
        given()
                .when()
                .post("/quarkus-xsender/bill-service/send-voided-document")
                .then()
                .statusCode(200)
                .body(is(FakeSunat.TICKET));
    }
}
