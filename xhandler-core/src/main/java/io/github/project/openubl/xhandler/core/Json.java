package io.github.project.openubl.xhandler.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The JSON mapping shared with the xhandler core.
 */
public final class Json {

    /**
     * Dates as "YYYY-MM-DD", times as "HH:MM:SS" and decimals as plain
     * strings, so that their scale is kept ("118.00" stays "118.00", never
     * "1.18E+2"). Unknown properties fail, so a model out of sync with the
     * core is noticed. Do not reconfigure it: it is the wire format.
     */
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule()
                    .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss"))))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);

    static {
        MAPPER.configOverride(BigDecimal.class).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING));
    }

    private Json() {
    }
}
