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
