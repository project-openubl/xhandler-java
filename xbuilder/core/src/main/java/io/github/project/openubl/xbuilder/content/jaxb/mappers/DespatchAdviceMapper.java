package io.github.project.openubl.xbuilder.content.jaxb.mappers;

import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.FirmanteMapper;
import java.util.List;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.Numero2Translator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdviceLine;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdviceItem;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Comprador;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Tercero;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Destinatario;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Destino;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoBaja;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoRelacionado;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoAdicional;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Envio;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Partida;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Remitente;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Transportista;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Driver;
import io.github.project.openubl.xbuilder.content.models.standard.guia.GuiaItemAttribute;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Puerto;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Vehicle;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
        SerieNumeroMapper.class,
        FirmanteMapper.class
}, nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface DespatchAdviceMapper {

    @Mapping(target = "serie", source = "documentId", qualifiedBy = { SerieNumeroTranslator.class, SerieTranslator.class })
    @Mapping(target = "numero", source = "documentId", qualifiedBy = { SerieNumeroTranslator.class, Numero2Translator.class })
    @Mapping(target = "version", source = "customizationId")
    @Mapping(target = "fechaEmision", source = "issueDate")
    @Mapping(target = "horaEmision", source = "issueTime")
    @Mapping(target = "tipoComprobante", source = "despatchAdviceTypeCode")
    @Mapping(target = "observaciones", source = "note")
    @Mapping(target = "documentoBaja", source = "orderReference")
    @Mapping(target = "documentoRelacionado", ignore = true)
    @Mapping(target = "firmante", source = "signature")
    @Mapping(target = "remitente", source = "despatchSupplierParty")
    @Mapping(target = "destinatario", source = "deliveryCustomerParty")
    @Mapping(target = "proveedor", source = "sellerSupplierParty", qualifiedByName = "mapDespatchAdviceProveedor")
    @Mapping(target = "tercero", source = "sellerSupplierParty", qualifiedByName = "mapDespatchAdviceTercero")
    @Mapping(target = "envio", source = "shipment")
    @Mapping(target = "detalles", source = "lines")
    @Mapping(target = "documentosAdicionales", source = "additionalDocumentReferences", qualifiedByName = "mapDocumentosAdicionales")
    @Mapping(target = "comprador", source = "buyerCustomerParty")
    @Mapping(target = "documentoAdicional", ignore = true)
    @Mapping(target = "detalle", ignore = true)
    DespatchAdvice map(XMLDespatchAdvice xml);

    @Mapping(target = "tipoDocumento", source = "orderTypeCode")
    @Mapping(target = "serieNumero", source = "id")
    DocumentoBaja mapDocumentoBaja(XMLDespatchAdvice.OrderReference xml);

    @Mapping(target = "tipoDocumento", source = "documentTypeCode")
    @Mapping(target = "serieNumero", source = "id")
    DocumentoRelacionado mapDocumentoRelacionado(XMLDespatchAdvice.AdditionalDocumentReference xml);

    @Mapping(target = "ruc", source = "party.partyIdentification.id.value")
    @Mapping(target = "razonSocial", source = "party.partyLegalEntity.registrationName")
    @Mapping(target = "numeroRegistroMTC", source = "party.partyLegalEntity.companyID")
    Remitente mapRemitente(XMLDespatchAdvice.DespatchSupplierParty xml);

    @Mapping(target = "tipo", source = "jobTitle")
    @Mapping(target = "tipoDocumentoIdentidad", source = "id.schemeID")
    @Mapping(target = "numeroDocumentoIdentidad", source = "id.value")
    @Mapping(target = "nombres", source = "firstName")
    @Mapping(target = "apellidos", source = "familyName")
    @Mapping(target = "licencia", source = "identityDocumentReference.id")
    Driver mapDriver(XMLDespatchAdvice.DriverPerson xml);

    @Mapping(target = "numeroDocumentoIdentidad", source = "party.partyIdentification.id.value")
    @Mapping(target = "tipoDocumentoIdentidad", source = "party.partyIdentification.id.schemeID")
    @Mapping(target = "nombre", source = "party.partyLegalEntity.registrationName")
    Destinatario mapDestinatario(XMLDespatchAdvice.DeliveryCustomerParty xml);

    @Named("mapDespatchAdviceProveedor")
    default Proveedor mapDespatchAdviceProveedor(XMLDespatchAdvice.SellerSupplierParty xml) {
        if (xml == null || xml.getCustomerAssignedAccountId() == null) {
            return null;
        }
        Proveedor.ProveedorBuilder builder = Proveedor.builder();
        builder.ruc(xml.getCustomerAssignedAccountId());
        if (xml.getParty() != null && xml.getParty().getPartyLegalEntity() != null) {
            builder.razonSocial(xml.getParty().getPartyLegalEntity().getRegistrationName());
            builder.nombreComercial(xml.getParty().getPartyLegalEntity().getRegistrationName());
        }
        return builder.build();
    }

    @Named("mapDespatchAdviceTercero")
    default Tercero mapDespatchAdviceTercero(XMLDespatchAdvice.SellerSupplierParty xml) {
        if (xml == null || xml.getParty() == null || xml.getParty().getPartyIdentification() == null) {
            return null;
        }
        Tercero.TerceroBuilder builder = Tercero.builder();
        if (xml.getParty().getPartyIdentification().getId() != null) {
            builder.numeroDocumentoIdentidad(xml.getParty().getPartyIdentification().getId().getValue());
            builder.tipoDocumentoIdentidad(xml.getParty().getPartyIdentification().getId().getSchemeID());
        }
        if (xml.getParty().getPartyLegalEntity() != null) {
            builder.nombre(xml.getParty().getPartyLegalEntity().getRegistrationName());
        }
        return builder.build();
    }

    @Mapping(target = "tipoDocumentoIdentidad", source = "party.partyIdentification.id.schemeID")
    @Mapping(target = "numeroDocumentoIdentidad", source = "party.partyIdentification.id.value")
    @Mapping(target = "nombre", source = "party.partyLegalEntity.registrationName")
    Comprador mapComprador(XMLDespatchAdvice.BuyerCustomerParty xml);

    @Mapping(target = "tipoTraslado", source = "handlingCode")
    @Mapping(target = "motivoTraslado", source = "handlingInstructions")
    @Mapping(target = "pesoTotal", source = "grossWeightMeasure.value")
    @Mapping(target = "pesoTotalUnidadMedida", source = "grossWeightMeasure.unitCode")
    @Mapping(target = "numeroDeBultos", source = "totalTransportHandlingUnitQuantity")
    @Mapping(target = "tipoModalidadTraslado", source = "shipmentStage.transportModeCode")
    @Mapping(target = "fechaTraslado", source = "shipmentStage.transitPeriod.startDate")
    @Mapping(target = "transportista", source = "shipmentStage", conditionQualifiedByName = "transportistaRequirements")
    @Mapping(target = "destino", source = "delivery")
    @Mapping(target = "partida", source = ".", qualifiedByName = "mapPartidaFromShipment")
    @Mapping(target = "choferes", source = "shipmentStage.driverPersons", qualifiedByName = "mapChoferes")
    @Mapping(target = "indicadores", source = "specialInstructions", qualifiedByName = "mapIndicadores")
    @Mapping(target = "contenedores", source = "transportHandlingUnit", qualifiedByName = "mapContenedores")
    @Mapping(target = "pesoItems", source = "netWeightMeasure.value")
    @Mapping(target = "sustentoPeso", source = "information")
    @Mapping(target = "puerto", source = "firstArrivalPortLocation", qualifiedByName = "mapPuerto")
    @Mapping(target = "aeropuerto", source = "firstArrivalPortLocation", qualifiedByName = "mapAeropuerto")
    @Mapping(target = "vehiculo", source = "transportHandlingUnit", qualifiedByName = "mapVehiculo")
    @Mapping(target = "chofer", ignore = true)
    @Mapping(target = "indicador", ignore = true)
    @Mapping(target = "contenedor", ignore = true)
    Envio mapEnvio(XMLDespatchAdvice.Shipment xml);

    @Condition
    @Named("transportistaRequirements")
    default boolean conditionTransportista(XMLDespatchAdvice.ShipmentStage xml) {
        return xml.getCarrierParty() != null && xml.getTransportMeans() != null && xml.getDriverPersons() != null && !xml.getDriverPersons().isEmpty();
    }

    @Mapping(target = "tipoDocumentoIdentidad", source = "carrierParty.partyIdentification.id.schemeID")
    @Mapping(target = "numeroDocumentoIdentidad", source = "carrierParty.partyIdentification.id.value")
    @Mapping(target = "nombre", source = "carrierParty.partyLegalEntity.registrationName")
    @Mapping(target = "numeroRegistroMTC", source = "carrierParty.partyLegalEntity.companyID")
    Transportista mapTransportista(XMLDespatchAdvice.ShipmentStage xml);

    @Mapping(target = "ubigeo", source = "deliveryAddress.id")
    @Mapping(target = "direccion", source = "deliveryAddress.addressLine.line")
    @Mapping(target = "codigoLocal", source = "deliveryAddress.addressTypeCode.value")
    @Mapping(target = "ruc", source = "deliveryAddress.addressTypeCode.listID")
    Destino mapDelivery(XMLDespatchAdvice.Delivery xml);

    @Mapping(target = "ubigeo", source = "id")
    @Mapping(target = "direccion", source = "streetName")
    @Mapping(target = "codigoLocal", ignore = true)
    @Mapping(target = "ruc", ignore = true)
    Partida mapPartida(XMLDespatchAdvice.OriginAddress xml);

    @Mapping(target = "unidadMedida", source = "deliveredQuantity.unitCode")
    @Mapping(target = "cantidad", source = "deliveredQuantity.value")
    @Mapping(target = "descripcion", source = "item", qualifiedByName = "mapItemDescription")
    @Mapping(target = "codigo", source = "item.sellersItemIdentification.id")
    @Mapping(target = "codigoSunat", source = "item.commodityClassification.itemClassificationCode")
    @Mapping(target = "atributos", source = "item.additionalItemProperties", qualifiedByName = "mapAtributos")
    @Mapping(target = "atributo", ignore = true)
    DespatchAdviceItem mapLine(XMLDespatchAdviceLine xml);

    @Mapping(target = "code", source = "nameCode")
    GuiaItemAttribute mapAttribute(XMLDespatchAdviceLine.AdditionalItemProperty xml);

    @Named("mapItemDescription")
    default String mapItemDescription(XMLDespatchAdviceLine.Item item) {
        if (item == null) {
            return null;
        }
        // Prefer description (new format) over name (old format)
        return item.getDescription() != null ? item.getDescription() : item.getName();
    }

    @Named("mapPartidaFromShipment")
    default Partida mapPartidaFromShipment(XMLDespatchAdvice.Shipment shipment) {
        if (shipment == null) {
            return null;
        }

        // Try new format first: delivery.despatch.despatchAddress
        if (shipment.getDelivery() != null &&
                shipment.getDelivery().getDespatch() != null &&
                shipment.getDelivery().getDespatch().getDespatchAddress() != null) {
            XMLDespatchAdvice.DespatchAddress addr = shipment.getDelivery().getDespatch().getDespatchAddress();
            return Partida.builder()
                    .ubigeo(addr.getId())
                    .direccion(addr.getAddressLine() != null ? addr.getAddressLine().getLine() : null)
                    .codigoLocal(addr.getAddressTypeCode() != null ? addr.getAddressTypeCode().getValue() : null)
                    .ruc(addr.getAddressTypeCode() != null ? addr.getAddressTypeCode().getListID() : null)
                    .build();
        }

        return null;
    }

    @Named("mapPrimaryDriverTipo")
    default String mapPrimaryDriverTipo(List<XMLDespatchAdvice.DriverPerson> drivers) {
        if (drivers == null || drivers.isEmpty())
            return null;
        return drivers.get(0).getId() != null ? drivers.get(0).getId().getSchemeID() : null;
    }

    @Named("mapPrimaryDriverNum")
    default String mapPrimaryDriverNum(List<XMLDespatchAdvice.DriverPerson> drivers) {
        if (drivers == null || drivers.isEmpty())
            return null;
        return drivers.get(0).getId() != null ? drivers.get(0).getId().getValue() : null;
    }

    @Named("mapContenedores")
    default List<String> mapContenedores(List<XMLDespatchAdvice.TransportHandlingUnit> units) {
        if (units == null)
            return java.util.Collections.emptyList();
        List<String> result = new java.util.ArrayList<>();
        for (XMLDespatchAdvice.TransportHandlingUnit unit : units) {
            if (unit.getPackages() != null) {
                unit.getPackages().stream().map(XMLDespatchAdvice.Package::getTraceID)
                        .filter(java.util.Objects::nonNull)
                        .forEach(result::add);
            }
            if (unit.getTransportEquipments() != null) {
                // Only add as container if it's NOT a vehicle (simple ID, no transport means)
                unit.getTransportEquipments().stream()
                        .filter(e -> e.getApplicableTransportMeans() == null)
                        .map(XMLDespatchAdvice.TransportEquipment::getId)
                        .filter(java.util.Objects::nonNull)
                        .forEach(result::add);
            }
        }
        return result;
    }

    @Named("mapChoferes")
    default List<Driver> mapChoferes(List<XMLDespatchAdvice.DriverPerson> drivers) {
        if (drivers == null)
            return java.util.Collections.emptyList();
        return drivers.stream().map(this::mapDriver).collect(java.util.stream.Collectors.toList());
    }

    @Named("mapIndicadores")
    default List<String> mapIndicadores(List<String> indicators) {
        if (indicators == null)
            return java.util.Collections.emptyList();
        return indicators;
    }

    @Named("mapAtributos")
    default List<GuiaItemAttribute> mapAtributos(List<XMLDespatchAdviceLine.AdditionalItemProperty> attrs) {
        if (attrs == null)
            return java.util.Collections.emptyList();
        return attrs.stream().map(this::mapAttribute).collect(java.util.stream.Collectors.toList());
    }

    @Named("mapDocumentosAdicionales")
    default List<DocumentoAdicional> mapDocumentosAdicionales(
            List<XMLDespatchAdvice.AdditionalDocumentReference> rels) {
        if (rels == null)
            return java.util.Collections.emptyList();
        return rels.stream().map(this::mapDocumentoAdicional).collect(java.util.stream.Collectors.toList());
    }

    @Named("mapPuerto")
    default Puerto mapPuerto(XMLDespatchAdvice.FirstArrivalPortLocation xml) {
        if (xml == null || !"1".equals(xml.getLocationTypeCode()))
            return null;
        return Puerto.builder().codigo(xml.getId()).nombre(xml.getName()).build();
    }

    @Named("mapAeropuerto")
    default Puerto mapAeropuerto(XMLDespatchAdvice.FirstArrivalPortLocation xml) {
        if (xml == null || !"2".equals(xml.getLocationTypeCode()))
            return null;
        return Puerto.builder().codigo(xml.getId()).nombre(xml.getName()).build();
    }

    @Named("mapVehiculo")
    default Vehicle mapVehiculo(List<XMLDespatchAdvice.TransportHandlingUnit> units) {
        if (units == null)
            return null;
        // The first equipment with transport means or multiple are usually vehicles
        return units.stream()
                .filter(u -> u.getTransportEquipments() != null)
                .flatMap(u -> u.getTransportEquipments().stream())
                .filter(e -> e.getApplicableTransportMeans() != null || e.getAttachedTransportEquipments() != null)
                .findFirst()
                .map(this::mapDetailedVehicle)
                .orElse(null);
    }

    default Vehicle mapDetailedVehicle(XMLDespatchAdvice.TransportEquipment xml) {
        if (xml == null)
            return null;
        Vehicle.VehicleBuilder builder = Vehicle.builder().placa(xml.getId());
        if (xml.getApplicableTransportMeans() != null) {
            builder.numeroCirculacion(xml.getApplicableTransportMeans().getRegistrationNationalityID());
        }
        if (xml.getShipmentDocumentReferences() != null && !xml.getShipmentDocumentReferences().isEmpty()) {
            XMLDespatchAdvice.ShipmentDocumentReference doc = xml.getShipmentDocumentReferences().get(0);
            if (doc.getId() != null) {
                builder.numeroAutorizacion(doc.getId().getValue());
                builder.codigoEmisor(doc.getId().getSchemeID());
            }
        }
        if (xml.getAttachedTransportEquipments() != null) {
            builder.secundarios(xml.getAttachedTransportEquipments().stream()
                    .map(this::mapDetailedVehicle)
                    .collect(java.util.stream.Collectors.toList()));
        }
        return builder.build();
    }

    @Mapping(target = "tipoDocumento", source = "documentTypeCode")
    @Mapping(target = "tipoDocumentoDescripcion", source = "documentType")
    @Mapping(target = "rucEmisor", source = "issuerParty.partyIdentification.id.value")
    @Mapping(target = "numero", source = "id")
    DocumentoAdicional mapDocumentoAdicional(XMLDespatchAdvice.AdditionalDocumentReference xml);

    @Named("mapPrimaryAdditionalDocument")
    default DocumentoRelacionado mapPrimaryAdditionalDocument(
            List<XMLDespatchAdvice.AdditionalDocumentReference> rels) {
        if (rels == null || rels.isEmpty())
            return null;
        return mapDocumentoRelacionado(rels.get(0));
    }
}
