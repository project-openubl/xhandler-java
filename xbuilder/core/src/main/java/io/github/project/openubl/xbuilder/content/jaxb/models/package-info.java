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