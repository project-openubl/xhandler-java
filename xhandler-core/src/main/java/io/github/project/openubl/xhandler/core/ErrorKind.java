package io.github.project.openubl.xhandler.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classifies the errors reported by the xhandler core.
 */
public enum ErrorKind {
    /** The request did not reach an operation. */
    @JsonProperty("invalid_request")
    INVALID_REQUEST,
    /** The input has missing fields or wrong types. */
    @JsonProperty("invalid_input")
    INVALID_INPUT,
    /** The document has invalid values, e.g. an unknown catalog code. */
    @JsonProperty("validation")
    VALIDATION,
    /** The XML could not be rendered. */
    @JsonProperty("render")
    RENDER,
    /**
     * The private key or certificate can't be read (e.g. a wrong keystore
     * password), or the certificate is not the one of the private key.
     */
    @JsonProperty("invalid_key")
    INVALID_KEY,
    /** The answer of SUNAT can't be read, e.g. an HTML error page. */
    @JsonProperty("response")
    RESPONSE,
    /** SUNAT's front proxy kept refusing the request (rate limiting); it was already retried. */
    @JsonProperty("rate_limited")
    RATE_LIMITED
}
