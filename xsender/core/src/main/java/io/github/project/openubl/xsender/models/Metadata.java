package io.github.project.openubl.xsender.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Metadata {
    private final Integer responseCode;
    private final String description;
    private final List<String> notes;
}
