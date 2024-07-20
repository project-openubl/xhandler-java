package io.github.project.openubl.xsender.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CompanyURLs {

    private final String invoice;
    private final String perceptionRetention;
    private final String despatch;
}
