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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Retention", namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class XMLRetention extends XMLPercepcionRetencionBase {
    @XmlElement(name = "SUNATRetentionSystemCode", namespace = XMLConstants.SAC)
    private String sunatSystemCode;

    @XmlElement(name = "SUNATRetentionPercent", namespace = XMLConstants.SAC)
    private BigDecimal sunatPercent;

    @XmlElement(name = "SUNATTotalPaid", namespace = XMLConstants.SAC)
    private BigDecimal sunatTotal;

    @XmlElement(name = "SUNATRetentionDocumentReference", namespace = XMLConstants.SAC)
    private XMLRetentionSunatDocumentReference sunatDocumentReference;
}
