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
@XmlRootElement(name = "Retention", namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLRetention extends XMLPercepcionRetencionBase {
    @XmlElement(name = "SUNATRetentionSystemCode", namespace = XMLConstants.SAC)
    private String sunatSystemCode;

    @XmlElement(name = "SUNATRetentionPercent", namespace = XMLConstants.SAC)
    private BigDecimal sunatPercent;

    @XmlElement(name = "SUNATTotalPaid", namespace = XMLConstants.SAC)
    private BigDecimal sunatTotal;

    @XmlElement(name = "SUNATRetentionDocumentReference", namespace = XMLConstants.SAC)
    private XMLRetentionSunatDocumentReference sunatDocumentReference;
}
