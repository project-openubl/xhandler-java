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

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "XMLPercepcionRetencionSunatDocumentReferenceBase")
@Data
@NoArgsConstructor
public abstract class XMLPercepcionRetencionSunatDocumentReferenceBase {

    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private ID id;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "IssueDate", namespace = XMLConstants.CBC)
    private LocalDate issueDate;

    @XmlElement(name = "TotalInvoiceAmount", namespace = XMLConstants.CBC)
    private TotalInvoiceAmount totalInvoiceAmount;

    @XmlElement(name = "Payment", namespace = XMLConstants.CAC)
    private Payment payment;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "XMLPercepcionRetencionSunatDocumentReferenceBase.ID")
    @Data
    @NoArgsConstructor
    public static class ID {
        @XmlValue
        private String value;

        @XmlAttribute(name = "schemeID")
        private String schemeID;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "XMLPercepcionRetencionSunatDocumentReferenceBase.TotalInvoiceAmount")
    @Data
    @NoArgsConstructor
    public static class TotalInvoiceAmount {
        @XmlValue
        private BigDecimal value;

        @XmlAttribute(name = "currencyID")
        private String currencyID;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "XMLPercepcionRetencionSunatDocumentReferenceBase.Payment")
    @Data
    @NoArgsConstructor
    public static class Payment {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private Integer id;

        @XmlElement(name = "PaidAmount", namespace = XMLConstants.CBC)
        private BigDecimal paidAmount;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "PaidDate", namespace = XMLConstants.CBC)
        private LocalDate paidDate;
    }
}
