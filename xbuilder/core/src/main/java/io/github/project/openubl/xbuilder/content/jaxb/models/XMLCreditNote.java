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
@XmlRootElement(name = "CreditNote", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLCreditNote extends XMLSalesDocument {
    @XmlElement(name = "LegalMonetaryTotal", namespace = XMLConstants.CAC)
    private MonetaryTotal monetaryTotal;

    @XmlElement(name = "CreditNoteLine", namespace = XMLConstants.CAC)
    private List<XMLCreditNoteLine> lines;
}
