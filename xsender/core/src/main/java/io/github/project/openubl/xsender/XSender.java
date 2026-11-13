package io.github.project.openubl.xsender;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.BillServiceXMLFileAnalyzer;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import io.github.project.openubl.xsender.sunat.SunatClient;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        BillServiceXMLFileAnalyzer analyzer = analyze(xml);
        return sunatClient.sendFile(analyzer.getSendFileDestination(), analyzer.getZipFile(), credentials);
    }

    public SunatResponse send(Path xml) {
        return send(readAllBytes(xml));
    }

    /**
     * @param xml    the document the ticket was given for
     * @param ticket returned when the document was sent
     */
    public SunatResponse verifyTicket(byte[] xml, String ticket) {
        BillServiceDestination destination = analyze(xml).getVerifyTicketDestination();
        if (destination == null) {
            throw new XSenderException("SUNAT does not give tickets for this type of document");
        }
        return sunatClient.verifyTicket(destination, ticket, credentials);
    }

    public SunatResponse verifyTicket(Path xml, String ticket) {
        return verifyTicket(readAllBytes(xml), ticket);
    }

    private BillServiceXMLFileAnalyzer analyze(byte[] xml) {
        try {
            return new BillServiceXMLFileAnalyzer(xml, urls);
        } catch (Exception e) {
            throw new XSenderException("Could not read the document to send: " + e.getMessage(), e);
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
