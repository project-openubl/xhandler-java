package io.github.project.openubl.xsender.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ZipFile {
    private final byte[] file;
    private final String filename;
}
