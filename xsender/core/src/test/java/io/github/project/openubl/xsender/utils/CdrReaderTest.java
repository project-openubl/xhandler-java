package io.github.project.openubl.xsender.utils;

import io.github.project.openubl.xsender.models.Status;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CdrReaderTest {

    private static final String CDR = """
            <?xml version="1.0" encoding="UTF-8"?>
            <ar:ApplicationResponse xmlns:ar="urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2"
                                    xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
                                    xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2">
                <cbc:ID>1537453143191</cbc:ID>
                <cbc:Note>4252 - El dato ingresado como atributo @listName es incorrecto.</cbc:Note>
                <cbc:Note>4287 - El precio unitario ingresado es incorrecto.</cbc:Note>
                <cac:SenderParty><cac:PartyIdentification><cbc:ID>20131312955</cbc:ID></cac:PartyIdentification></cac:SenderParty>
                <cac:DocumentResponse>
                    <cac:Response>
                        <cbc:ReferenceID>F001-1</cbc:ReferenceID>
                        <cbc:ResponseCode>0</cbc:ResponseCode>
                        <cbc:Description>La Factura numero F001-1, ha sido aceptada</cbc:Description>
                    </cac:Response>
                </cac:DocumentResponse>
            </ar:ApplicationResponse>
            """;

    @Test
    void readsCodeDescriptionAndNotes() throws Exception {
        CdrReader cdr = new CdrReader(zip("dummy/", "R-12345678912-01-F001-1.xml", CDR));

        assertEquals(Status.ACEPTADO, cdr.getStatus());
        assertEquals(0, cdr.getMetadata().getResponseCode());
        assertEquals("La Factura numero F001-1, ha sido aceptada", cdr.getMetadata().getDescription());
        assertEquals(List.of(
                "4252 - El dato ingresado como atributo @listName es incorrecto.",
                "4287 - El precio unitario ingresado es incorrecto."
        ), cdr.getMetadata().getNotes());
    }

    @Test
    void cdrWithoutDescription() throws Exception {
        String withoutDescription = CDR.replaceAll("<cbc:Description>.*</cbc:Description>", "");

        CdrReader cdr = new CdrReader(zip("dummy/", "R.xml", withoutDescription));

        assertEquals("", cdr.getMetadata().getDescription());
    }

    @Test
    void zipWithoutXml_throws() {
        assertThrows(IOException.class, () -> new CdrReader(zip("dummy/", "readme.txt", "x")));
    }

    private static byte[] zip(String directory, String name, String content) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
            zip.putNextEntry(new ZipEntry(directory));
            zip.closeEntry();
            zip.putNextEntry(new ZipEntry(name));
            zip.write(content.getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();
        }
        return bytes.toByteArray();
    }
}
