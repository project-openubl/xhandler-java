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
package io.github.project.openubl.quarkus.xsender.it;

import io.github.project.openubl.xsender.models.Status;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
                .body(is(notNullValue()));
    }

    @Test
    public void consultService_getStatus() {
        given()
                .when()
                .post("/quarkus-xsender/consult-service/get-status")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    public void consultService_getStatusCdr() {
        given()
                .when()
                .post("/quarkus-xsender/consult-service/get-status-cdr")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    public void sendDespatchAdvice() {
        given()
                .when()
                .post("/quarkus-xsender/bill-service/send-despatch-advice")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    public void consultValidService_validateData() {
        given()
                .when()
                .post("/quarkus-xsender/consult-valid-service/validate-data")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    public void consultValidService_validateFile() {
        given()
                .when()
                .post("/quarkus-xsender/consult-valid-service/validate-file")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }
}
