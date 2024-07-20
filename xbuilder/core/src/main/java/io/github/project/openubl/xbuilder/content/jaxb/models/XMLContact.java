package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Contact")
@Data
@NoArgsConstructor
public class XMLContact {

    @XmlElement(name = "Telephone", namespace = XMLConstants.CBC)
    private String telephone;

    @XmlElement(name = "ElectronicMail", namespace = XMLConstants.CBC)
    private String electronicMail;
}
