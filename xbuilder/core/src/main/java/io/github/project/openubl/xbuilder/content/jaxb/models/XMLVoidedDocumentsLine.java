package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "VoidedDocumentsLine")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public class XMLVoidedDocumentsLine {

    @XmlElement(name = "DocumentTypeCode", namespace = XMLConstants.CBC)
    private String documentTypeCode;

    @XmlElement(name = "DocumentSerialID", namespace = XMLConstants.SAC)
    private String documentSerialID;

    @XmlElement(name = "DocumentNumberID", namespace = XMLConstants.SAC)
    private Integer documentNumberID;

    @XmlElement(name = "VoidReasonDescription", namespace = XMLConstants.SAC)
    private String voidReasonDescription;
}
