package io.github.project.openubl.xsender.camel.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CamelData {
    private Object body;
    private Map<String, Object> headers;
}
