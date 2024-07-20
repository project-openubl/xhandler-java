package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Signature")
@Data
@NoArgsConstructor
public class XMLSignature {
    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private String id;

    @XmlElement(name = "SignatoryParty", namespace = XMLConstants.CAC)
    private SignatoryParty signatoryParty;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "Signature.SignatoryParty")
    @Data
    @NoArgsConstructor
    public static class SignatoryParty {
        @XmlElement(name = "PartyName", namespace = XMLConstants.CAC)
        private PartyName partyName;
    }

    @XmlType(name = "Signature.PartyName")
    @XmlAccessorType(XmlAccessType.NONE)
    @Data
    @NoArgsConstructor
    public static class PartyName {
        @XmlElement(name = "Name", namespace = XMLConstants.CBC)
        private String name;
    }
}
