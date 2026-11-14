package io.github.project.openubl.quarkus.xbuilder.it;

import io.github.project.openubl.xhandler.core.Conformance;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class QuarkusXbuilderResourceTest {

    static List<Conformance.Case> buildCases() {
        return Conformance.cases("build").stream().filter(c -> !c.expectsError()).toList();
    }

    @ParameterizedTest
    @MethodSource("buildCases")
    public void buildsTheCorpus(Conformance.Case c) {
        given()
                .contentType(ContentType.JSON)
                .body(c.input().toString())
                .when()
                .post("/quarkus-xbuilder/build")
                .then()
                .statusCode(200)
                .body(is(c.expected().get("ok").asText()));
    }

    @Test
    public void usesTheConfiguredRates() {
        String invoice = """
                {"kind": "Invoice", "spec": {
                  "serie_numero": "F001-1",
                  "proveedor": {"ruc": "12345678912", "razon_social": "Softgreen S.A.C."},
                  "cliente": {"tipo_documento_identidad": "6", "numero_documento_identidad": "12121212121", "nombre": "Carlos Feria"},
                  "detalles": [{"descripcion": "Item", "cantidad": "1", "precio": "100"}]
                }}""";
        given()
                .contentType(ContentType.JSON)
                .body(invoice)
                .when()
                .post("/quarkus-xbuilder/enrich")
                .then()
                .statusCode(200)
                .body("igv_tasa", is("0.2"));
    }
}
