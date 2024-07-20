package io.github.project.openubl.xbuilder.content.jaxb.models;

import io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalDateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlType(name = "PerceptionRetentionBase")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public abstract class XMLPercepcionRetencionBase {

    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private String documentId;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "IssueDate", namespace = XMLConstants.CBC)
    private LocalDate issueDate;

    @XmlElement(name = "Note", namespace = XMLConstants.CBC)
    private String note;

    @XmlElement(name = "TotalInvoiceAmount", namespace = XMLConstants.CBC)
    private TotalInvoiceAmount totalInvoiceAmount;

    @XmlElement(name = "Signature", namespace = XMLConstants.CAC)
    private XMLSignature signature;

    @XmlElement(name = "AgentParty", namespace = XMLConstants.CAC)
    private XMLSupplier accountingSupplierParty;

    @XmlElement(name = "ReceiverParty", namespace = XMLConstants.CAC)
    private XMLCustomer accountingCustomerParty;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "PerceptionRetentionBase.TotalInvoiceAmount")
    @Data
    @NoArgsConstructor
    public static class TotalInvoiceAmount {
        @XmlValue
        private BigDecimal value;

        @XmlAttribute(name = "currencyID")
        private String currencyID;
    }
}
