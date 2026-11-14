package io.github.project.openubl.xhandler.core;

/**
 * An error reported by the xhandler core.
 */
public class XhandlerException extends RuntimeException {

    private final ErrorKind kind;

    public XhandlerException(ErrorKind kind, String message) {
        super("xhandler " + kind.name().toLowerCase() + ": " + message);
        this.kind = kind;
    }

    public XhandlerException(String message, Throwable cause) {
        super(message, cause);
        this.kind = null;
    }

    /**
     * @return the kind of error, or {@code null} when the core could not be reached
     */
    public ErrorKind getKind() {
        return kind;
    }
}
