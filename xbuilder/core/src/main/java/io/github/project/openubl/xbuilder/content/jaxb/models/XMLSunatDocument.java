/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
