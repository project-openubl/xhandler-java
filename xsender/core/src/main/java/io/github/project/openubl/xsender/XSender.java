package io.github.project.openubl.xsender;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xhandler.core.Xhandler;
import io.github.project.openubl.xhandler.core.XhandlerException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import io.github.project.openubl.xsender.sunat.Destinations;
import io.github.project.openubl.xsender.sunat.SunatClient;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Sends signed UBL documents of one company to SUNAT. The XML decides
 * everything else: the file name, the service, and the operation.
 *
 * <pre>{@code
 * XSender xsender = new XSender(urls, credentials);
 * Path xml = Path.of("RA-20200328-1.xml");
 * SunatResponse response = xsender.send(xml);
 * if (response.isTicket()) {
 *     response = xsender.verifyTicket(xml, response.getSunat().getTicket());
 * }
 * }</pre>
 * Applications sending for many companies should share one {@link SunatClient}
 * between their XSenders.
 */
public class XSender {

    private final CompanyURLs urls;
    private final CompanyCredentials credentials;
    private final SunatClient sunatClient;

    public XSender(CompanyURLs urls, CompanyCredentials credentials) {
        this(urls, credentials, new SunatClient());
    }

    public XSender(CompanyURLs urls, CompanyCredentials credentials, SunatClient sunatClient) {
        this.urls = urls;
        this.credentials = credentials;
        this.sunatClient = sunatClient;
    }

    /**
     * @return the CDR, a ticket to {@link #verifyTicket verify} later, or SUNAT's error
     */
    public SunatResponse send(byte[] xml) {
        return send(core("prepare_send_file", xmlInput(xml)));
    }

    private SunatResponse send(JsonNode prepared) {
        JsonNode file = prepared.get("file");
        ZipFile zipFile = new ZipFile(Base64.getDecoder().decode(file.get("zip").asText()), file.get("filename").asText());
        return sunatClient.sendFile(Destinations.sendFile(prepared.get("send_target")), zipFile, credentials);
    }

    public SunatResponse send(Path xml) {
        return send(readAllBytes(xml));
    }

    /**
     * Sends several invoices in one zip (sendPack).
     *
     * @param name name of the zip, without ".zip"
     * @return a ticket to verify later, or SUNAT's error
     */
    public SunatResponse sendPack(String name, List<byte[]> xmls) {
        Map<String, Object> input = Map.of(
                "name", name,
                "files", xmls.stream().map(Base64.getEncoder()::encodeToString).toList(),
                "urls", urlsJson()
        );
        return send(core("prepare_send_pack", input));
    }

    /**
     * @param xml    the document the ticket was given for
     * @param ticket returned when the document was sent
     */
    public SunatResponse verifyTicket(byte[] xml, String ticket) {
        BillServiceDestination destination = Destinations.verifyTicket(core("verify_ticket_target", xmlInput(xml)));
        return sunatClient.verifyTicket(destination, ticket, credentials);
    }

    public SunatResponse verifyTicket(Path xml, String ticket) {
        return verifyTicket(readAllBytes(xml), ticket);
    }

    private Map<String, Object> xmlInput(byte[] xml) {
        return Map.of("xml", Base64.getEncoder().encodeToString(xml), "urls", urlsJson());
    }

    private Map<String, Object> urlsJson() {
        return Map.of(
                "invoice", Objects.toString(urls.getInvoice(), ""),
                "perception_retention", Objects.toString(urls.getPerceptionRetention(), ""),
                "despatch", Objects.toString(urls.getDespatch(), "")
        );
    }

    /** Asks the xhandler core how the documents go to SUNAT */
    private static JsonNode core(String op, Map<String, Object> input) {
        try {
            return Xhandler.invoke(op, input);
        } catch (XhandlerException e) {
            throw new XSenderException("Could not send the document: " + e.getMessage(), e);
        }
    }

    private static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
