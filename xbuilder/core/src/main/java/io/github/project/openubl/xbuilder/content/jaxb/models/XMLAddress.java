package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "RegistrationAddress")
@Data
@NoArgsConstructor
public class XMLAddress {

    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private String id;

    @XmlElement(name = "AddressTypeCode", namespace = XMLConstants.CBC)
    private String addressTypeCode;

    @XmlElement(name = "CitySubdivisionName", namespace = XMLConstants.CBC)
    private String citySubdivisionName;

    @XmlElement(name = "CityName", namespace = XMLConstants.CBC)
    private String cityName;

    @XmlElement(name = "CountrySubentity", namespace = XMLConstants.CBC)
    private String countrySubEntity;

    @XmlElement(name = "District", namespace = XMLConstants.CBC)
    private String district;

    @XmlElement(name = "AddressLine", namespace = XMLConstants.CAC)
    private AddressLine addressLine;

    @XmlElement(name = "Country", namespace = XMLConstants.CAC)
    private Country country;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "Address.AddressLine")
    @Data
    @NoArgsConstructor
    public static class AddressLine {
        @XmlElement(name = "Line", namespace = XMLConstants.CBC)
        private String line;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "Address.Country")
    @Data
    @NoArgsConstructor
    public static class Country {
        @XmlElement(name = "IdentificationCode", namespace = XMLConstants.CBC)
        private String identificationCode;
    }
}
