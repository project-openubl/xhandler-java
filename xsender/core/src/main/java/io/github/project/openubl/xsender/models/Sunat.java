package io.github.project.openubl.xsender.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sunat {
    private String ticket;
    private byte[] cdr;
}
