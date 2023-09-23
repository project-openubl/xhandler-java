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
