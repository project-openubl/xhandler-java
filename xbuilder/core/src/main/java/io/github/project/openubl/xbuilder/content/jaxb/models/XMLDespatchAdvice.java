package io.github.project.openubl.xbuilder.content.jaxb.models;

import io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalDateAdapter;
import io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalTimeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DespatchAdvice", namespace = "urn:oasis:names:specification:ubl:schema:xsd:DespatchAdvice-2")
@Data
@NoArgsConstructor
public class XMLDespatchAdvice {

    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private String documentId;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "IssueDate", namespace = XMLConstants.CBC)
    private LocalDate issueDate;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    @XmlElement(name = "IssueTime", namespace = XMLConstants.CBC)
    private LocalTime issueTime;

    @XmlElement(name = "DespatchAdviceTypeCode", namespace = XMLConstants.CBC)
    private String despatchAdviceTypeCode;

    @XmlElement(name = "Note", namespace = XMLConstants.CBC)
    private String note;

    @XmlElement(name = "OrderReference", namespace = XMLConstants.CAC)
    private OrderReference orderReference;

    @XmlElement(name = "AdditionalDocumentReference", namespace = XMLConstants.CAC)
    private AdditionalDocumentReference additionalDocumentReference;

    @XmlElement(name = "Signature", namespace = XMLConstants.CAC)
    private XMLSignature signature;

    @XmlElement(name = "DespatchSupplierParty", namespace = XMLConstants.CAC)
    private DespatchSupplierParty despatchSupplierParty;

    @XmlElement(name = "DeliveryCustomerParty", namespace = XMLConstants.CAC)
    private DeliveryCustomerParty deliveryCustomerParty;

    @XmlElement(name = "SellerSupplierParty", namespace = XMLConstants.CAC)
    private SellerSupplierParty sellerSupplierParty;

    @XmlElement(name = "Shipment", namespace = XMLConstants.CAC)
    private Shipment shipment;

    @XmlElement(name = "DespatchLine", namespace = XMLConstants.CAC)
    private List<XMLDespatchAdviceLine> lines;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.OrderReference")
    @Data
    @NoArgsConstructor
    public static class OrderReference {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "OrderTypeCode", namespace = XMLConstants.CBC)
        private String orderTypeCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.AdditionalDocumentReference")
    @Data
    @NoArgsConstructor
    public static class AdditionalDocumentReference {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "DocumentTypeCode", namespace = XMLConstants.CBC)
        private String documentTypeCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.DespatchSupplierParty")
    @Data
    @NoArgsConstructor
    public static class DespatchSupplierParty {
        @XmlElement(name = "Party", namespace = XMLConstants.CAC)
        private Party party;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.Party")
    @Data
    @NoArgsConstructor
    public static class Party {
        @XmlElement(name = "PartyIdentification", namespace = XMLConstants.CAC)
        private PartyIdentification partyIdentification;

        @XmlElement(name = "PartyLegalEntity", namespace = XMLConstants.CAC)
        private PartyLegalEntity partyLegalEntity;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.PartyIdentification")
    @Data
    @NoArgsConstructor
    public static class PartyIdentification {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private ID id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.PartyIdentification.ID")
    @Data
    @NoArgsConstructor
    public static class ID {
        @XmlValue
        private String value;

        @XmlAttribute(name = "schemeID")
        private String schemeID;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.PartyLegalEntity")
    @Data
    @NoArgsConstructor
    public static class PartyLegalEntity {
        @XmlElement(name = "RegistrationName", namespace = XMLConstants.CBC)
        private String registrationName;
    }


    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.DeliveryCustomerParty")
    @Data
    @NoArgsConstructor
    public static class DeliveryCustomerParty {
        @XmlElement(name = "Party", namespace = XMLConstants.CAC)
        private Party party;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.SellerSupplierParty")
    @Data
    @NoArgsConstructor
    public static class SellerSupplierParty {
        @XmlElement(name = "CustomerAssignedAccountID", namespace = XMLConstants.CBC)
        private String customerAssignedAccountId;

        @XmlElement(name = "Party", namespace = XMLConstants.CAC)
        private Party party;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.Shipment")
    @Data
    @NoArgsConstructor
    public static class Shipment {
        @XmlElement(name = "HandlingCode", namespace = XMLConstants.CBC)
        private String handlingCode;

        @XmlElement(name = "Information", namespace = XMLConstants.CBC)
        private String information;

        @XmlElement(name = "GrossWeightMeasure", namespace = XMLConstants.CBC)
        private GrossWeightMeasure grossWeightMeasure;

        @XmlElement(name = "TotalTransportHandlingUnitQuantity", namespace = XMLConstants.CBC)
        private Integer totalTransportHandlingUnitQuantity;

        @XmlElement(name = "SplitConsignmentIndicator", namespace = XMLConstants.CBC)
        private Boolean splitConsignmentIndicator;

        @XmlElement(name = "ShipmentStage", namespace = XMLConstants.CAC)
        private ShipmentStage shipmentStage;

        @XmlElement(name = "Delivery", namespace = XMLConstants.CAC)
        private Delivery delivery;

        @XmlElement(name = "TransportHandlingUnit", namespace = XMLConstants.CAC)
        private TransportHandlingUnit transportHandlingUnit;

        @XmlElement(name = "OriginAddress", namespace = XMLConstants.CAC)
        private OriginAddress originAddress;

        @XmlElement(name = "FirstArrivalPortLocation", namespace = XMLConstants.CAC)
        private FirstArrivalPortLocation firstArrivalPortLocation;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.GrossWeightMeasure")
    @Data
    @NoArgsConstructor
    public static class GrossWeightMeasure {
        @XmlValue
        private BigDecimal value;

        @XmlAttribute(name = "unitCode")
        private String unitCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.ShipmentStage")
    @Data
    @NoArgsConstructor
    public static class ShipmentStage {
        @XmlElement(name = "TransportModeCode", namespace = XMLConstants.CBC)
        private String transportModeCode;

        @XmlElement(name = "TransitPeriod", namespace = XMLConstants.CAC)
        private TransitPeriod transitPeriod;

        @XmlElement(name = "CarrierParty", namespace = XMLConstants.CAC)
        private CarrierParty carrierParty;

        @XmlElement(name = "TransportMeans", namespace = XMLConstants.CAC)
        private TransportMeans transportMeans;

        @XmlElement(name = "DriverPerson", namespace = XMLConstants.CAC)
        private DriverPerson driverPerson;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.TransitPeriod")
    @Data
    @NoArgsConstructor
    public static class TransitPeriod {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "StartDate", namespace = XMLConstants.CBC)
        private LocalDate startDate;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.CarrierParty")
    @Data
    @NoArgsConstructor
    public static class CarrierParty {
        @XmlElement(name = "PartyIdentification", namespace = XMLConstants.CAC)
        private PartyIdentification partyIdentification;

        @XmlElement(name = "PartyName", namespace = XMLConstants.CAC)
        private PartyName partyName;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.PartyName")
    @Data
    @NoArgsConstructor
    public static class PartyName {
        @XmlElement(name = "Name", namespace = XMLConstants.CBC)
        private String name;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.TransportMeans")
    @Data
    @NoArgsConstructor
    public static class TransportMeans {
        @XmlElement(name = "RoadTransport", namespace = XMLConstants.CAC)
        private RoadTransport roadTransport;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.RoadTransport")
    @Data
    @NoArgsConstructor
    public static class RoadTransport {
        @XmlElement(name = "LicensePlateID", namespace = XMLConstants.CBC)
        private String licensePlateID;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.DriverPerson")
    @Data
    @NoArgsConstructor
    public static class DriverPerson {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private ID id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.Delivery")
    @Data
    @NoArgsConstructor
    public static class Delivery {
        @XmlElement(name = "DeliveryAddress", namespace = XMLConstants.CAC)
        private DeliveryAddress deliveryAddress;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.DeliveryAddress")
    @Data
    @NoArgsConstructor
    public static class DeliveryAddress {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "AddressLine", namespace = XMLConstants.CAC)
        private AddressLine addressLine;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.AddressLine")
    @Data
    @NoArgsConstructor
    public static class AddressLine {
        @XmlElement(name = "Line", namespace = XMLConstants.CBC)
        private String line;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.TransportHandlingUnit")
    @Data
    @NoArgsConstructor
    public static class TransportHandlingUnit {
        @XmlElement(name = "TransportEquipment", namespace = XMLConstants.CAC)
        private TransportEquipment transportEquipment;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.TransportEquipment")
    @Data
    @NoArgsConstructor
    public static class TransportEquipment {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.OriginAddress")
    @Data
    @NoArgsConstructor
    public static class OriginAddress {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "StreetName", namespace = XMLConstants.CBC)
        private String streetName;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "DespatchAdvice.FirstArrivalPortLocation")
    @Data
    @NoArgsConstructor
    public static class FirstArrivalPortLocation {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }
}
