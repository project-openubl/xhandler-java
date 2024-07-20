package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SummaryDocuments", namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLSummaryDocuments extends XMLSunatDocument {

    @XmlElement(name = "SummaryDocumentsLine", namespace = XMLConstants.SAC)
    private List<XMLSummaryDocumentsLine> lines;

}
