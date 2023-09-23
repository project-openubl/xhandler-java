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

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.List;

@XmlType(name = "SalesDocumentLine")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public class XMLSalesDocumentLine {

    @XmlElement(name = "LineExtensionAmount", namespace = XMLConstants.CBC)
    private BigDecimal lineExtensionAmount;

    @XmlElement(name = "PricingReference", namespace = XMLConstants.CAC)
    private PricingReference pricingReference;

    @XmlElement(name = "TaxTotal", namespace = XMLConstants.CAC)
    private TaxTotalLine taxTotal;

    @XmlElement(name = "Item", namespace = XMLConstants.CAC)
    private Item item;

    @XmlElement(name = "Price", namespace = XMLConstants.CAC)
    private Price price;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.PricingReference")
    @Data
    @NoArgsConstructor
    public static class PricingReference {
        @XmlElement(name = "AlternativeConditionPrice", namespace = XMLConstants.CAC)
        private AlternativeConditionPrice alternativeConditionPrice;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.AlternativeConditionPrice")
    @Data
    @NoArgsConstructor
    public static class AlternativeConditionPrice {
        @XmlElement(name = "PriceAmount", namespace = XMLConstants.CBC)
        private BigDecimal alternativeConditionPrice;

        @XmlElement(name = "PriceTypeCode", namespace = XMLConstants.CBC)
        private String priceTypeCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.TaxTotalLine")
    @Data
    @NoArgsConstructor
    public static class TaxTotalLine {
        @XmlElement(name = "TaxAmount", namespace = XMLConstants.CBC)
        BigDecimal taxAmount;

        @XmlElement(name = "TaxSubtotal", namespace = XMLConstants.CAC)
        List<TaxSubtotalLine> taxSubtotals;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.TaxSubtotalLine")
    @Data
    @NoArgsConstructor
    public static class TaxSubtotalLine {
        @XmlElement(name = "TaxableAmount", namespace = XMLConstants.CBC)
        private BigDecimal taxableAmount;

        @XmlElement(name = "TaxAmount", namespace = XMLConstants.CBC)
        private BigDecimal taxAmount;

        @XmlElement(name = "TaxCategory", namespace = XMLConstants.CAC)
        private TaxCategory taxCategory;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.TaxCategory")
    @Data
    @NoArgsConstructor
    public static class TaxCategory {
        @XmlElement(name = "Percent", namespace = XMLConstants.CBC)
        private BigDecimal percent;

        @XmlElement(name = "TierRange", namespace = XMLConstants.CBC)
        private String tierRange;

        @XmlElement(name = "PerUnitAmount", namespace = XMLConstants.CBC)
        private BigDecimal perUnitAmount;

        @XmlElement(name = "TaxExemptionReasonCode", namespace = XMLConstants.CBC)
        private String taxExemptionReasonCode;

        @XmlElement(name = "TaxScheme", namespace = XMLConstants.CAC)
        private TaxScheme TaxScheme;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.TaxScheme")
    @Data
    @NoArgsConstructor
    public static class TaxScheme {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.Item")
    @Data
    @NoArgsConstructor
    public static class Item {
        @XmlElement(name = "Description", namespace = XMLConstants.CBC)
        private String description;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocumentLine.Price")
    @Data
    @NoArgsConstructor
    public static class Price {
        @XmlElement(name = "PriceAmount", namespace = XMLConstants.CBC)
        private BigDecimal priceAmount;
    }
}
