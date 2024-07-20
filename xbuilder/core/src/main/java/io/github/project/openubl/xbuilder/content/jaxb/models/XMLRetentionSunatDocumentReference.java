package io.github.project.openubl.xbuilder.content.jaxb.models;

import io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalDateAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "XMLRetentionSunatDocumentReference")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLRetentionSunatDocumentReference extends XMLPercepcionRetencionSunatDocumentReferenceBase {

    @XmlElement(name = "SUNATRetentionInformation", namespace = XMLConstants.SAC)
    private XMLRetentionInformation sunatInformation;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "XMLRetentionSunatDocumentReference.XMLRetencionInformation")
    @Data
    @NoArgsConstructor
    public static class XMLRetentionInformation {
        @XmlElement(name = "SUNATRetentionAmount", namespace = XMLConstants.SAC)
        private BigDecimal sunatAmount;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "SUNATRetentionDate", namespace = XMLConstants.SAC)
        private LocalDate sunatDate;

        @XmlElement(name = "SUNATNetTotalPaid", namespace = XMLConstants.SAC)
        private BigDecimal sunatNetTotal;

        @XmlElement(name = "ExchangeRate", namespace = XMLConstants.CAC)
        private ExchangeRate exchangeRate;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "XMLRetentionSunatDocumentReference.ExchangeRate")
    @Data
    @NoArgsConstructor
    public static class ExchangeRate {
        @XmlElement(name = "CalculationRate", namespace = XMLConstants.CBC)
        private BigDecimal calculationRate;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "Date", namespace = XMLConstants.CBC)
        private LocalDate date;
    }
}
