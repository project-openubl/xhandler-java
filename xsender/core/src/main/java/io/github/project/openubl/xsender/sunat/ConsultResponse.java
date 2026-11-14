package io.github.project.openubl.xsender.sunat;

/**
 * The answer of SUNAT's consult and validation services, as SUNAT gives it.
 * Its codes are specific to each service (e.g. "0001" for "La constancia existe"),
 * so they are not mapped to a {@link io.github.project.openubl.xsender.models.Status}.
 *
 * @param statusCode    SUNAT's code, or the error code of a SOAP fault
 * @param statusMessage SUNAT's message, or the fault message
 * @param cdr           the zipped CDR when SUNAT returns one, otherwise null. Being an array,
 *                      it is not part of equals/hashCode.
 * @param httpStatus    the HTTP status of a SOAP fault (e.g. 500), otherwise null
 */
public record ConsultResponse(String statusCode, String statusMessage, byte[] cdr, Integer httpStatus) {
}
