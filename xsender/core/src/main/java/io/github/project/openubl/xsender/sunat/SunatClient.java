package io.github.project.openubl.xsender.sunat;

import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

/**
 * Calls SUNAT's services: the SOAP bill, consult and validation services, and
 * the REST API for guías de remisión.
 * <p>
 * Thread-safe and meant to be shared (one per application): it holds the HTTP
 * connection pool and caches the guías de remisión access tokens.
 */
public class SunatClient {

    public static final String SUNAT_SECURITY_API_URL = "https://api-seguridad.sunat.gob.pe";

    private final HttpClient httpClient;
    private final GreClient greClient;

    public SunatClient() {
        this(SUNAT_SECURITY_API_URL);
    }

    /**
     * @param securityApiUrl where the guías de remisión access tokens are requested
     */
    public SunatClient(String securityApiUrl) {
        this(defaultHttpClient(), securityApiUrl);
    }

    /**
     * @param securityApiUrl where the guías de remisión access tokens are requested
     */
    public SunatClient(HttpClient httpClient, String securityApiUrl) {
        this.httpClient = httpClient;
        this.greClient = new GreClient(httpClient, securityApiUrl);
    }

    static HttpClient defaultHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    /**
     * Sends a zipped document: sendBill/sendSummary/sendPack, or the REST API for guías de remisión.
     *
     * @return the CDR, a ticket to verify later, or SUNAT's error
     */
    public SunatResponse sendFile(BillServiceDestination destination, ZipFile zipFile, CompanyCredentials credentials) {
        if (destination.getRestOperation() != null) {
            return greClient.sendDocument(destination, zipFile, credentials);
        }

        String operation = destination.getSoapOperation().getWebMethod();
        String zipBase64 = Base64.getEncoder().encodeToString(zipFile.getFile());
        SoapResponse response = callSoap(destination.getUrl(), operation,
                SoapEnvelopes.sendFile(operation, credentials, zipFile.getFilename(), zipBase64));

        if (response.isFault()) {
            return faultResponse(response);
        }
        return response.get("applicationResponse")
                .map(cdr -> SunatResponses.cdr(Base64.getMimeDecoder().decode(cdr)))
                .or(() -> response.get("ticket").map(SunatResponses::ticket))
                .orElseThrow(() -> new XSenderException("SUNAT answered " + operation + " with neither a CDR nor a ticket"));
    }

    /**
     * Checks the ticket SUNAT returned for a summary, voided documents or a guía de remisión.
     *
     * @return the CDR once processed, status {@code EN_PROCESO} meanwhile, or SUNAT's error
     */
    public SunatResponse verifyTicket(BillServiceDestination destination, String ticket, CompanyCredentials credentials) {
        if (destination.getRestOperation() != null) {
            return greClient.verifyTicket(destination, ticket, credentials);
        }

        SoapResponse response = callSoap(destination.getUrl(), "getStatus", SoapEnvelopes.getStatus(credentials, ticket));
        if (response.isFault()) {
            return faultResponse(response);
        }
        return response.get("content")
                .map(cdr -> SunatResponses.cdr(Base64.getMimeDecoder().decode(cdr)))
                .orElseGet(() -> SunatResponses.status(
                        response.get("statusCode").orElse(null), response.get("statusMessage").orElse(null), List.of()
                ));
    }

    /**
     * Consults an issued document: its status (getStatus) or its CDR (getStatusCdr).
     */
    public ConsultResponse consultDocument(BillConsultServiceDestination destination, DocumentQuery query, CompanyCredentials credentials) {
        String operation = destination.getOperation().getWebMethod();
        return consultResponse(callSoap(destination.getUrl(), operation,
                SoapEnvelopes.consultDocument(operation, credentials, query)));
    }

    /**
     * Validates a document against the given criteria (validaCDPcriterios).
     */
    public ConsultResponse validateCdp(BillValidServiceDestination destination, CdpQuery query, CompanyCredentials credentials) {
        return consultResponse(callSoap(destination.getUrl(), "validaCDPcriterios",
                SoapEnvelopes.validateCdp(credentials, query)));
    }

    /**
     * Validates an XML file (verificaCPEarchivo).
     */
    public ConsultResponse validateFile(BillValidServiceDestination destination, String filename, byte[] file, CompanyCredentials credentials) {
        String fileBase64 = Base64.getEncoder().encodeToString(file);
        return consultResponse(callSoap(destination.getUrl(), "verificaCPEarchivo",
                SoapEnvelopes.validateFile(credentials, filename, fileBase64)));
    }

    /**
     * SUNAT answers SOAP faults with HTTP 500, so any status with a SOAP body is a response.
     */
    private SoapResponse callSoap(String url, String operation, String envelope) {
        HttpRequest request = Http.request(url)
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", "urn:" + operation)
                .POST(HttpRequest.BodyPublishers.ofString(envelope))
                .build();
        HttpResponse<byte[]> response = Http.send(httpClient, request);
        return SoapResponse.parse(response.body()).orElseThrow(() -> Http.unexpectedResponse(response));
    }

    private static SunatResponse faultResponse(SoapResponse response) {
        return SunatResponses.fault(response.get("faultcode").orElse(null), response.get("faultstring").orElse(null));
    }

    private static ConsultResponse consultResponse(SoapResponse response) {
        if (response.isFault()) {
            String faultString = response.get("faultstring").orElse(null);
            return new ConsultResponse(
                    SunatResponses.faultErrorCode(response.get("faultcode").orElse(null), faultString), faultString, null
            );
        }
        byte[] cdr = response.get("content").map(Base64.getMimeDecoder()::decode).orElse(null);
        return new ConsultResponse(response.get("statusCode").orElse(null), response.get("statusMessage").orElse(null), cdr);
    }
}
