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
@XmlType(name = "XMLPercepcionSunatDocumentReference")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLPercepcionSunatDocumentReference extends XMLPercepcionRetencionSunatDocumentReferenceBase {

    @XmlElement(name = "SUNATPerceptionInformation", namespace = XMLConstants.SAC)
    private XMLPercepcionInformation sunatInformation;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "XMLPercepcionSunatDocumentReference.XMLPercepcionInformation")
    @Data
    @NoArgsConstructor
    public static class XMLPercepcionInformation {
        @XmlElement(name = "SUNATPerceptionAmount", namespace = XMLConstants.SAC)
        private BigDecimal sunatAmount;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "SUNATPerceptionDate", namespace = XMLConstants.SAC)
        private LocalDate sunatDate;

        @XmlElement(name = "SUNATNetTotalCashed", namespace = XMLConstants.SAC)
        private BigDecimal sunatNetTotal;

        @XmlElement(name = "ExchangeRate", namespace = XMLConstants.CAC)
        private ExchangeRate exchangeRate;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "XMLPercepcionSunatDocumentReference.ExchangeRate")
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
