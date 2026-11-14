package io.github.project.openubl.xsender;

import io.github.project.openubl.xhandler.core.ErrorKind;
import io.github.project.openubl.xhandler.core.XhandlerException;

/**
 * The document could not be sent or SUNAT's answer could not be understood
 * (network failure, unsupported XML, unexpected HTTP response...).
 * <p>
 * Answers SUNAT does give, including rejections and SOAP faults, are not
 * exceptions: they come back as a {@link io.github.project.openubl.xsender.models.SunatResponse}.
 */
public class XSenderException extends RuntimeException {

    private final ErrorKind kind;

    public XSenderException(String message) {
        this(message, (Throwable) null);
    }

    public XSenderException(String message, Throwable cause) {
        super(message, cause);
        this.kind = cause instanceof XhandlerException xhandler ? xhandler.getKind() : null;
    }

    /**
     * @return why the xhandler core failed, e.g. {@link ErrorKind#RATE_LIMITED} when SUNAT
     * kept refusing the request or {@link ErrorKind#RESPONSE} for an answer that can't be
     * read; {@code null} when the core was not involved (e.g. SUNAT can't be reached)
     */
    public ErrorKind getKind() {
        return kind;
    }
}
