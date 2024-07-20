package io.github.project.openubl.xbuilder.content.jaxb.models;

import io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalDateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlType(name = "SunatDocument")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public abstract class XMLSunatDocument {

    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private String documentId;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "ReferenceDate", namespace = XMLConstants.CBC)
    private LocalDate referenceDate;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "IssueDate", namespace = XMLConstants.CBC)
    private LocalDate issueDate;

    @XmlElement(name = "Signature", namespace = XMLConstants.CAC)
    private XMLSignature signature;

    @XmlElement(name = "AccountingSupplierParty", namespace = XMLConstants.CAC)
    private XMLSupplierSunat accountingSupplierParty;

}
