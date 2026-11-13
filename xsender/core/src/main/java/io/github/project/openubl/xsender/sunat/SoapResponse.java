package io.github.project.openubl.xsender.sunat;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The values of a SUNAT SOAP response, by element local name. SUNAT's
 * responses are flat enough for that: a CDR in {@code applicationResponse}
 * or {@code content}, a {@code ticket}, a {@code statusCode}/{@code statusMessage}
 * or a fault's {@code faultcode}/{@code faultstring}.
 */
record SoapResponse(Map<String, String> values) {

    private static final String SOAP_ENVELOPE_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final XMLInputFactory XML_INPUT_FACTORY = createInputFactory();

    /**
     * @return empty if the body is not a SOAP envelope, e.g. an HTML error page
     */
    static Optional<SoapResponse> parse(byte[] body) {
        try {
            XMLStreamReader reader = XML_INPUT_FACTORY.createXMLStreamReader(new ByteArrayInputStream(body));
            try {
                reader.nextTag();
                if (!SOAP_ENVELOPE_NS.equals(reader.getNamespaceURI()) || !"Envelope".equals(reader.getLocalName())) {
                    return Optional.empty();
                }
                return Optional.of(new SoapResponse(readLeafValues(reader)));
            } finally {
                reader.close();
            }
        } catch (XMLStreamException e) {
            return Optional.empty();
        }
    }

    Optional<String> get(String localName) {
        return Optional.ofNullable(values.get(localName)).filter(value -> !value.isBlank());
    }

    boolean isFault() {
        return values.containsKey("faultcode") || values.containsKey("faultstring");
    }

    /** First text of every element without child elements, by local name */
    private static Map<String, String> readLeafValues(XMLStreamReader reader) throws XMLStreamException {
        Map<String, String> values = new HashMap<>();
        String element = null;
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT -> {
                    element = reader.getLocalName();
                    text.setLength(0);
                }
                case XMLStreamReader.CHARACTERS, XMLStreamReader.CDATA -> text.append(reader.getText());
                case XMLStreamReader.END_ELEMENT -> {
                    if (reader.getLocalName().equals(element)) {
                        values.putIfAbsent(element, text.toString().trim());
                    }
                    element = null;
                }
                default -> {
                }
            }
        }
        return values;
    }

    private static XMLInputFactory createInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        return factory;
    }
}
