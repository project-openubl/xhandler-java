package io.github.project.openubl.xsender.models.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDocumentErrorDto {
    private String cod;
    private String msg;
    private String exc;

    private String status;
    private String message;

    @Singular
    private List<Error> errors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String cod;
        private String msg;
    }
}
