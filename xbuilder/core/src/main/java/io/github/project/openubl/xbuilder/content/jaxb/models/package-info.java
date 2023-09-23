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
@XmlSchema(
        xmlns = {
                @XmlNs(prefix = "cbc", namespaceURI = XMLConstants.CBC),
                @XmlNs(prefix = "cac", namespaceURI = XMLConstants.CAC),
                @XmlNs(prefix = "sac", namespaceURI = XMLConstants.SAC),
                @XmlNs(prefix = "ext", namespaceURI = XMLConstants.EXT)
        },
        elementFormDefault = XmlNsForm.QUALIFIED
)
package io.github.project.openubl.xbuilder.content.jaxb.models;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;