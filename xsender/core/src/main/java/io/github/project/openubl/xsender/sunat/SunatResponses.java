package io.github.project.openubl.xsender.sunat;

import io.github.project.openubl.xsender.XSenderException;
import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.Sunat;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.utils.CdrReader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds {@link SunatResponse}s from the pieces SUNAT answers with.
 */
final class SunatResponses {

    /** The code in a SOAP faultcode like "soap-env:Client.0111" */
    private static final Pattern FAULT_CODE = Pattern.compile("(?:.*:)?(?:Client\\.|Server\\.)?(\\d+)");
    private static final Pattern NUMBER = Pattern.compile("\\d+");

    private SunatResponses() {
    }

    static SunatResponse cdr(byte[] zippedCdr) {
        CdrReader cdrReader;
        try {
            cdrReader = new CdrReader(zippedCdr);
        } catch (Exception e) {
            throw new XSenderException("Could not read the CDR returned by SUNAT", e);
        }
        return SunatResponse.builder()
                .status(cdrReader.getStatus())
                .metadata(cdrReader.getMetadata())
                .sunat(Sunat.builder().cdr(zippedCdr).build())
                .build();
    }

    static SunatResponse ticket(String ticket) {
        return SunatResponse.builder()
                .sunat(Sunat.builder().ticket(ticket).build())
                .build();
    }

    /**
     * A status without CDR, e.g. 98 while SUNAT is still processing a ticket
     */
    static SunatResponse status(String code, String description, List<String> notes) {
        Integer responseCode = parseCode(code);
        if (description == null && Integer.valueOf(98).equals(responseCode)) {
            description = "En proceso";
        }
        return SunatResponse.builder()
                .status(responseCode != null ? Status.fromCode(responseCode) : Status.UNKNOWN)
                .metadata(new Metadata(responseCode, description, notes))
                .build();
    }

    static SunatResponse fault(String faultCode, String faultString) {
        return status(faultErrorCode(faultCode, faultString), faultString, List.of());
    }

    /**
     * SUNAT puts its error code in the faultcode ("soap-env:Client.0111") or,
     * sometimes, as the whole faultstring ("0306").
     */
    static String faultErrorCode(String faultCode, String faultString) {
        Matcher matcher = FAULT_CODE.matcher(faultCode == null ? "" : faultCode.trim());
        if (matcher.matches()) {
            return matcher.group(1);
        }
        if (faultString != null && NUMBER.matcher(faultString.trim()).matches()) {
            return faultString.trim();
        }
        return null;
    }

    private static Integer parseCode(String code) {
        return code != null && NUMBER.matcher(code).matches() ? Integer.valueOf(code) : null;
    }
}
