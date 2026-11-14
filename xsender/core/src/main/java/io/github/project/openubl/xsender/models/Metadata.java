package io.github.project.openubl.xsender.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Metadata {
    /** SUNAT's code: of the CDR, the ticket, or the error (e.g. 1033) */
    private final Integer responseCode;
    private final String description;
    private final List<String> notes;
    /** HTTP status of SUNAT's error answer (e.g. 500 for SOAP faults, 422 for REST errors); null otherwise */
    private final Integer httpStatus;
}
