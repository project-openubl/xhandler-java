package io.github.project.openubl.xsender.models.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDocumentSuccessDto {
    private String numTicket;
    private ZonedDateTime fecRecepcion;

    private String codRespuesta;
    private String arcCdr;
    private String indCdrGenerado;
    private Error error;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String numError;
        private String desError;
    }
}
