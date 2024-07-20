package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Supplier.Party")
@Data
@NoArgsConstructor
public class XMLSupplier {

    @XmlElement(name = "PartyIdentification", namespace = XMLConstants.CAC)
    private PartyIdentification partyIdentification;

    @XmlElement(name = "PartyName", namespace = XMLConstants.CAC)
    private PartyName partyName;

    @XmlElement(name = "PartyLegalEntity", namespace = XMLConstants.CAC)
    private PartyLegalEntity partyLegalEntity;

    @XmlElement(name = "Contact", namespace = XMLConstants.CAC)
    private XMLContact contact;

    @XmlType(name = "Supplier.PartyIdentification")
    @XmlAccessorType(XmlAccessType.NONE)
    @Data
    @NoArgsConstructor
    public static class PartyIdentification {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }

    @XmlType(name = "Supplier.PartyName")
    @XmlAccessorType(XmlAccessType.NONE)
    @Data
    @NoArgsConstructor
    public static class PartyName {
        @XmlElement(name = "Name", namespace = XMLConstants.CBC)
        private String name;
    }

    @XmlType(name = "Supplier.PartyLegalEntity")
    @XmlAccessorType(XmlAccessType.NONE)
    @Data
    @NoArgsConstructor
    public static class PartyLegalEntity {
        @XmlElement(name = "RegistrationName", namespace = XMLConstants.CBC)
        private String registrationName;

        @XmlElement(name = "RegistrationAddress", namespace = XMLConstants.CAC)
        private XMLAddress address;
    }
}
