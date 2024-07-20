package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SupplierSunat")
@Data
@NoArgsConstructor
public class XMLSupplierSunat {

    @XmlElement(name = "CustomerAssignedAccountID", namespace = XMLConstants.CBC)
    private String customerAssignedAccountID;

    @XmlElement(name = "PartyName", namespace = XMLConstants.CAC)
    private PartyName partyName;

    @XmlElement(name = "Party", namespace = XMLConstants.CAC)
    private Party party;

    @XmlElement(name = "Contact", namespace = XMLConstants.CAC)
    private XMLContact contact;

    @XmlType(name = "SupplierSunat.PartyName")
    @XmlAccessorType(XmlAccessType.NONE)
    @Data
    @NoArgsConstructor
    public static class PartyName {
        @XmlElement(name = "Name", namespace = XMLConstants.CBC)
        private String name;
    }

    @XmlType(name = "SupplierSunat.Party")
    @XmlAccessorType(XmlAccessType.NONE)
    @Data
    @NoArgsConstructor
    public static class Party {
        @XmlElement(name = "PartyLegalEntity", namespace = XMLConstants.CAC)
        private PartyLegalEntity partyLegalEntity;
    }

    @XmlType(name = "SupplierSunat.PartyLegalEntity")
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
