package io.github.project.openubl.xsender;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xhandler.core.Xhandler;
import io.github.project.openubl.xhandler.core.XhandlerException;
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
 * FileSender sender = new FileSender(URLs.beta(), credentials);
 * Path xml = Path.of("RA-20200328-1.xml");
 * SunatResponse response = sender.sendFile(xml);
 * if (response.isTicket()) {
 *     response = sender.verifyFileTicket(xml, response.getSunat().getTicket());
 * }
 * }</pre>
 * Applications sending for many companies should share one {@link SunatClient}
 * between their FileSenders.
 * <p>
 * Errors of the xhandler core are {@link XhandlerException}s with their
 * {@link io.github.project.openubl.xhandler.core.ErrorKind}, e.g. {@code INVALID_INPUT}
 * for an XML that is not a SUNAT document; see {@link SunatClient} for the others.
 */
public class FileSender {

    private final URLs urls;
    private final Credentials credentials;
    private final SunatClient sunatClient;

    public FileSender(URLs urls, Credentials credentials) {
        this(urls, credentials, new SunatClient());
    }

    public FileSender(URLs urls, Credentials credentials, SunatClient sunatClient) {
        this.urls = urls;
        this.credentials = credentials;
        this.sunatClient = sunatClient;
    }

    /**
     * @return the CDR, a ticket to {@link #verifyFileTicket verify} later, or SUNAT's error
     */
    public SunatResponse sendFile(byte[] xml) {
        return send(Xhandler.invoke("prepare_send_file", xmlInput(xml)));
    }

    private SunatResponse send(JsonNode prepared) {
        JsonNode file = prepared.get("file");
        ZipFile zipFile = new ZipFile(Base64.getDecoder().decode(file.get("zip").asText()), file.get("filename").asText());
        return sunatClient.sendFile(Destinations.sendFile(prepared.get("send_target")), zipFile, credentials);
    }

    public SunatResponse sendFile(Path xml) {
        return sendFile(readAllBytes(xml));
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
        return send(Xhandler.invoke("prepare_send_pack", input));
    }

    /**
     * Checks a ticket in the service of the document it was given for.
     *
     * @param xml    the document the ticket was given for
     * @param ticket returned when the document was sent
     */
    public SunatResponse verifyFileTicket(byte[] xml, String ticket) {
        return verifyTicket(Destinations.verifyTicket(Xhandler.invoke("verify_ticket_target", xmlInput(xml))), ticket);
    }

    public SunatResponse verifyFileTicket(Path xml, String ticket) {
        return verifyFileTicket(readAllBytes(xml), ticket);
    }

    /**
     * Checks a ticket in the given service.
     *
     * @param target e.g. the invoice service for summaries
     * @param ticket returned when the document was sent
     */
    public SunatResponse verifyTicket(BillServiceDestination target, String ticket) {
        return sunatClient.verifyTicket(target, ticket, credentials);
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

    private static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
