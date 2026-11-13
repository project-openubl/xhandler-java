package io.github.project.openubl.xsender;

/**
 * The document could not be sent or SUNAT's answer could not be understood
 * (network failure, unsupported XML, unexpected HTTP response...).
 * <p>
 * Answers SUNAT does give, including rejections and SOAP faults, are not
 * exceptions: they come back as a {@link io.github.project.openubl.xsender.models.SunatResponse}.
 */
public class XSenderException extends RuntimeException {

    public XSenderException(String message) {
        super(message);
    }

    public XSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
