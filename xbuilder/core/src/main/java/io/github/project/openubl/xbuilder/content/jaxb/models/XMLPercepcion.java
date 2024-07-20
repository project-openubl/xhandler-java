package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Perception", namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLPercepcion extends XMLPercepcionRetencionBase {

    @XmlElement(name = "SUNATPerceptionSystemCode", namespace = XMLConstants.SAC)
    private String sunatSystemCode;

    @XmlElement(name = "SUNATPerceptionPercent", namespace = XMLConstants.SAC)
    private BigDecimal sunatPercent;

    @XmlElement(name = "SUNATTotalCashed", namespace = XMLConstants.SAC)
    private BigDecimal sunatTotal;

    @XmlElement(name = "SUNATPerceptionDocumentReference", namespace = XMLConstants.SAC)
    private XMLPercepcionSunatDocumentReference sunatDocumentReference;
}
