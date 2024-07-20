package io.github.project.openubl.xsender.sunat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BillValidServiceDestination {
    private final String url;
}
