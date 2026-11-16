package io.github.project.openubl.xsender.sunat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xhandler.core.Json;
import io.github.project.openubl.xhandler.core.XhandlerException;
import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.Sunat;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.models.answers.CDR;
import io.github.project.openubl.xsender.models.answers.ConsultResult;
import io.github.project.openubl.xsender.models.answers.ConsultStatus;
import io.github.project.openubl.xsender.models.answers.Fault;
import io.github.project.openubl.xsender.models.answers.NoCDR;
import io.github.project.openubl.xsender.models.answers.SendFileAnswer;
import io.github.project.openubl.xsender.models.answers.VerifyResult;

import java.util.List;

/**
 * Builds the responses from the answers of the xhandler core, read into the
 * types generated from its schema (models.answers).
 */
final class SunatResponses {

    private SunatResponses() {
    }

    /** A CDR, a ticket to verify later, or SUNAT's error */
    static SunatResponse send(JsonNode json) {
        SendFileAnswer answer = read(json, SendFileAnswer.class);
        if (answer.getCdr() != null) {
            return of(answer.getCdr());
        }
        if (answer.getTicket() != null) {
            return SunatResponse.builder()
                    .sunat(Sunat.builder().ticket(answer.getTicket()).build())
                    .build();
        }
        return of(answer.getFault());
    }

    /** A CDR, no CDR yet (e.g. in progress), or SUNAT's error */
    static SunatResponse verify(JsonNode json) {
        VerifyResult answer = read(json, VerifyResult.class);
        if (answer.getCdr() != null) {
            return of(answer.getCdr());
        }
        NoCDR noCdr = answer.getNoCdr();
        if (noCdr != null) {
            Integer code = number(noCdr.getStatusCode());
            return SunatResponse.builder()
                    .status(status(noCdr.getStatus()))
                    .metadata(new Metadata(code, Integer.valueOf(98).equals(code) ? "En proceso" : null, List.of(), null))
                    .build();
        }
        return of(answer.getFault());
    }

    static ConsultResponse consult(JsonNode json) {
        ConsultResult answer = read(json, ConsultResult.class);
        Fault fault = answer.getFault();
        if (fault != null) {
            // SUNAT's error codes have 4 digits, e.g. "0111"
            String code = fault.getSunatCode() == null ? null : "%04d".formatted(fault.getSunatCode());
            return new ConsultResponse(code, fault.getMessage(), null, fault.getHttpStatus());
        }
        ConsultStatus status = answer.getStatus();
        return new ConsultResponse(status.getStatusCode(), status.getStatusMessage(), status.getCdrZip(), null);
    }

    private static SunatResponse of(CDR cdr) {
        return SunatResponse.builder()
                .status(status(cdr.getStatus()))
                .metadata(new Metadata(number(cdr.getResponseCode()), cdr.getDescription(), List.copyOf(cdr.getNotes()), null))
                .sunat(Sunat.builder()
                        .cdr(cdr.getZip())
                        .documentDescription(cdr.getDocumentDescription())
                        .build())
                .cdr(cdr)
                .build();
    }

    private static SunatResponse of(Fault fault) {
        Long code = fault.getSunatCode();
        return SunatResponse.builder()
                .status(status(fault.getStatus()))
                .metadata(new Metadata(
                        code == null ? null : code.intValue(),
                        fault.getMessage(),
                        List.of(),
                        fault.getHttpStatus()
                ))
                .build();
    }

    private static Status status(io.github.project.openubl.xsender.models.answers.Status status) {
        return switch (status) {
            case ACCEPTED -> Status.ACEPTADO;
            case REJECTED -> Status.RECHAZADO;
            case EXCEPTION -> Status.EXCEPCION;
            case IN_PROGRESS -> Status.EN_PROCESO;
            case UNKNOWN -> Status.UNKNOWN;
        };
    }

    /** The generated answers ignore the fields a newer core adds */
    private static <T> T read(JsonNode json, Class<T> type) {
        try {
            return Json.MAPPER.treeToValue(json, type);
        } catch (JsonProcessingException e) {
            throw new XhandlerException("xsender: " + e.getMessage(), e);
        }
    }

    private static Integer number(String code) {
        return code.matches("\\d+") ? Integer.valueOf(code) : null;
    }
}
