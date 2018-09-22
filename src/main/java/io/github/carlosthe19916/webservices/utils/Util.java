package io.github.carlosthe19916.webservices.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Util {

    private Util() {
        // Just static methods
    }

    public static Optional<Integer> getErrorCode(SOAPFaultException exception) {
        String errorCode = "";

        SOAPFault fault = exception.getFault();
        if (fault != null) {
            String faultCode = fault.getFaultCode();
            if (faultCode != null) {
                errorCode = faultCode.replaceAll("soap-env:Client.", "");
            }
        }

        if (!errorCode.matches("-?\\d+")) {
            String exceptionMessage = exception.getMessage();
            if (exceptionMessage != null) {
                errorCode = exceptionMessage.replaceAll("soap-env:Client.", "");
            }
        }

        if (!errorCode.matches("-?\\d+")) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(errorCode));
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

    public static byte[] getFirstXmlFileFromZip(byte[] data) throws IOException {
        try (
                ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data));
                ByteArrayOutputStream os = new ByteArrayOutputStream()
        ) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".xml")) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                    }
                    return os.toByteArray();
                }
            }

        }
        return null;
    }

    public static Document getDocumentFromBytes(byte[] cdrXml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(cdrXml));
    }

}
