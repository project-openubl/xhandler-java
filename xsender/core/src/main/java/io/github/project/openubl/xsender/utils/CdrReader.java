package io.github.project.openubl.xsender.utils;

import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import lombok.Data;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the response code, description and notes of a zipped CDR (ApplicationResponse).
 */
@Data
public class CdrReader {

    private static final String AR_NS = "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2";
    private static final String CAC_NS = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";
    private static final String CBC_NS = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";

    private static final List<String> RESPONSE_CODE = List.of(AR_NS + "|ApplicationResponse", CAC_NS + "|DocumentResponse", CAC_NS + "|Response", CBC_NS + "|ResponseCode");
    private static final List<String> DESCRIPTION = List.of(AR_NS + "|ApplicationResponse", CAC_NS + "|DocumentResponse", CAC_NS + "|Response", CBC_NS + "|Description");
    private static final List<String> NOTE = List.of(AR_NS + "|ApplicationResponse", CBC_NS + "|Note");

    private final byte[] zipFile;
    private final Status status;
    private final Metadata metadata;

    public CdrReader(byte[] zipFile) throws IOException, XMLStreamException {
        this.zipFile = zipFile;

        byte[] xml = ByteUtils.getFirstXmlFileFromZip(zipFile);
        if (xml == null) {
            throw new IOException("The CDR zip has no XML file");
        }

        this.metadata = readContent(xml);
        this.status = Status.fromCode(this.metadata.getResponseCode());
    }

    private static Metadata readContent(byte[] xml) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);

        String code = null;
        String description = null;
        List<String> notes = new ArrayList<>();

        List<String> path = new ArrayList<>();
        XMLStreamReader reader = factory.createXMLStreamReader(new ByteArrayInputStream(xml));
        try {
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamReader.START_ELEMENT) {
                    path.add(reader.getNamespaceURI() + "|" + reader.getLocalName());
                    if (path.equals(RESPONSE_CODE) && code == null) {
                        code = reader.getElementText().trim();
                        path.removeLast();
                    } else if (path.equals(DESCRIPTION) && description == null) {
                        description = reader.getElementText().trim();
                        path.removeLast();
                    } else if (path.equals(NOTE)) {
                        notes.add(reader.getElementText().trim());
                        path.removeLast();
                    }
                } else if (event == XMLStreamReader.END_ELEMENT) {
                    path.removeLast();
                }
            }
        } finally {
            reader.close();
        }

        if (code == null) {
            throw new XMLStreamException("The CDR has no ResponseCode");
        }
        return new Metadata(Integer.parseInt(code), description == null ? "" : description, notes);
    }
}
