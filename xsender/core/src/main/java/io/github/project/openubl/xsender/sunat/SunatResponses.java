package io.github.project.openubl.xsender.sunat;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.Sunat;
import io.github.project.openubl.xsender.models.SunatResponse;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Builds the responses from the answers of the xhandler core: {@code {cdr}},
 * {@code {ticket}}, {@code {no_cdr}}, {@code {fault}} or {@code {status}}.
 */
final class SunatResponses {

    private SunatResponses() {
    }

    static SunatResponse of(JsonNode answer) {
        if (answer.has("cdr")) {
            JsonNode cdr = answer.get("cdr");
            return SunatResponse.builder()
                    .status(Status.valueOf(cdr.get("status").asText()))
                    .metadata(new Metadata(number(cdr.get("response_code")), cdr.get("description").asText(), notes(cdr)))
                    .sunat(Sunat.builder().cdr(Base64.getDecoder().decode(cdr.get("zip").asText())).build())
                    .build();
        }
        if (answer.has("ticket")) {
            return SunatResponse.builder()
                    .sunat(Sunat.builder().ticket(answer.get("ticket").asText()).build())
                    .build();
        }
        if (answer.has("no_cdr")) {
            JsonNode noCdr = answer.get("no_cdr");
            Integer code = number(noCdr.get("status_code"));
            return SunatResponse.builder()
                    .status(Status.valueOf(noCdr.get("status").asText()))
                    .metadata(new Metadata(code, Integer.valueOf(98).equals(code) ? "En proceso" : null, List.of()))
                    .build();
        }
        JsonNode fault = answer.get("fault");
        return SunatResponse.builder()
                .status(Status.valueOf(fault.get("status").asText()))
                .metadata(new Metadata(fault.get("sunat_code").isNull() ? null : fault.get("sunat_code").asInt(), fault.get("message").asText(), List.of()))
                .build();
    }

    static ConsultResponse consult(JsonNode answer) {
        if (answer.has("fault")) {
            JsonNode fault = answer.get("fault");
            // SUNAT's error codes have 4 digits, e.g. "0111"
            String code = fault.get("sunat_code").isNull() ? null : "%04d".formatted(fault.get("sunat_code").asInt());
            return new ConsultResponse(code, fault.get("message").asText(), null);
        }
        JsonNode status = answer.get("status");
        JsonNode cdr = status.get("cdr_zip");
        return new ConsultResponse(
                status.get("status_code").asText(),
                status.get("status_message").isNull() ? null : status.get("status_message").asText(),
                cdr.isNull() ? null : Base64.getDecoder().decode(cdr.asText())
        );
    }

    private static Integer number(JsonNode code) {
        String text = code.asText();
        return text.matches("\\d+") ? Integer.valueOf(text) : null;
    }

    private static List<String> notes(JsonNode cdr) {
        List<String> notes = new ArrayList<>();
        cdr.get("notes").forEach(note -> notes.add(note.asText()));
        return notes;
    }
}
