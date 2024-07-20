package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import java.math.BigDecimal;

@XmlType(name = "InvoiceDocumentLine")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class XMLInvoiceLine extends XMLSalesDocumentLine {
    @XmlElement(name = "InvoicedQuantity", namespace = XMLConstants.CBC)
    private Quantity quantity;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "InvoiceDocumentLine.Quantity")
    @Data
    @NoArgsConstructor
    public static class Quantity {
        @XmlValue
        private BigDecimal value;

        @XmlAttribute(name = "unitCode")
        private String unitCode;
    }

}
