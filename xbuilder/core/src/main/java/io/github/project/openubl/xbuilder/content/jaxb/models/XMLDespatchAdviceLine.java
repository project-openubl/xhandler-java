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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

@XmlType(name = "DespatchAdviceLine")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public class XMLDespatchAdviceLine {

    @XmlElement(name = "DeliveredQuantity", namespace = XMLConstants.CBC)
    private DeliveredQuantity deliveredQuantity;

    @XmlElement(name = "Item", namespace = XMLConstants.CAC)
    private Item item;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdviceLine.DeliveredQuantity")
    @Data
    @NoArgsConstructor
    public static class DeliveredQuantity {
        @XmlValue
        private String value;

        @XmlAttribute(name = "unitCode")
        private String unitCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdviceLine.Item")
    @Data
    @NoArgsConstructor
    public static class Item {
        @XmlElement(name = "Name", namespace = XMLConstants.CBC)
        private String name;

        @XmlElement(name = "SellersItemIdentification", namespace = XMLConstants.CAC)
        private SellersItemIdentification sellersItemIdentification;

        @XmlElement(name = "CommodityClassification", namespace = XMLConstants.CAC)
        private CommodityClassification commodityClassification;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdviceLine.SellersItemIdentification")
    @Data
    @NoArgsConstructor
    public static class SellersItemIdentification {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdviceLine.CommodityClassification")
    @Data
    @NoArgsConstructor
    public static class CommodityClassification {
        @XmlElement(name = "ItemClassificationCode", namespace = XMLConstants.CBC)
        private String itemClassificationCode;
    }
}
