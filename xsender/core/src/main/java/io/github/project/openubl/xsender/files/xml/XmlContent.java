package io.github.project.openubl.xsender.files.xml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XmlContent {

    private String documentType;
    private String documentID;
    private String ruc;
    private String voidedLineDocumentTypeCode;
}
