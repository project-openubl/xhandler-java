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
@XmlRootElement(name = "DebitNote", namespace = "urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLDebitNote extends XMLSalesDocument {
    @XmlElement(name = "RequestedMonetaryTotal", namespace = XMLConstants.CAC)
    private MonetaryTotal monetaryTotal;

    @XmlElement(name = "DebitNoteLine", namespace = XMLConstants.CAC)
    private List<XMLDebitNoteLine> lines;
}
