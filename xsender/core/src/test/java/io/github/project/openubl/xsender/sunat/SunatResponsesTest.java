package io.github.project.openubl.xsender.sunat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SunatResponsesTest {

    private static final String QR = "https://e-factura.sunat.gob.pe/v1/contribuyente/gre/comprobantes/descargaqr?hashqr=abc";

    private static JsonNode cdr(String documentDescription) throws Exception {
        String field = documentDescription == null ? "" : ", \"document_description\": \"" + documentDescription + "\"";
        return new ObjectMapper().readTree("""
                {"cdr": {"zip": "", "response_code": "0", "description": "La Guia numero T001-1, ha sido aceptada",
                         "notes": [], "status": "ACEPTADO", "added_by_a_newer_core": true%s}}
                """.formatted(field));
    }

    @Test
    void cdr_carriesTheDocumentDescription() throws Exception {
        SunatResponse response = SunatResponses.send(cdr(QR));

        assertEquals(QR, response.getSunat().getDocumentDescription());
        assertEquals(QR, response.getCdr().getDocumentDescription());
    }

    @Test
    void cdr_withoutDocumentDescription() throws Exception {
        SunatResponse response = SunatResponses.send(cdr(null));

        assertNull(response.getSunat().getDocumentDescription());
        assertNull(response.getCdr().getDocumentDescription());
    }
}
