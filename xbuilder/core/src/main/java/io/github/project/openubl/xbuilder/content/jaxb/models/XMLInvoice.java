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
@XmlRootElement(name = "Invoice", namespace = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLInvoice extends XMLSalesDocument {
    @XmlElement(name = "LegalMonetaryTotal", namespace = XMLConstants.CAC)
    private MonetaryTotal monetaryTotal;

    @XmlElement(name = "InvoiceLine", namespace = XMLConstants.CAC)
    private List<XMLInvoiceLine> lines;
}
