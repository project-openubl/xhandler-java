package io.github.project.openubl.xbuilder.content.jaxb.mappers;

import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.FirmanteMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.Numero2Translator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdviceLine;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdviceItem;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Destinatario;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Destino;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoBaja;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoRelacionado;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Envio;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Partida;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Remitente;
import io.github.project.openubl.xbuilder.content.models.standard.guia.Transportista;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
        SerieNumeroMapper.class,
        FirmanteMapper.class
})
public interface DespatchAdviceMapper {

    @Mapping(target = "serie", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, SerieTranslator.class})
    @Mapping(target = "numero", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, Numero2Translator.class})
    @Mapping(target = "fechaEmision", source = "issueDate")
    @Mapping(target = "horaEmision", source = "issueTime")
    @Mapping(target = "tipoComprobante", source = "despatchAdviceTypeCode")
    @Mapping(target = "observaciones", source = "note")
    @Mapping(target = "documentoBaja", source = "orderReference")
    @Mapping(target = "documentoRelacionado", source = "additionalDocumentReference")
    @Mapping(target = "firmante", source = "signature")
    @Mapping(target = "remitente", source = "despatchSupplierParty")
    @Mapping(target = "destinatario", source = "deliveryCustomerParty")
    @Mapping(target = "proveedor", source = "sellerSupplierParty")
    @Mapping(target = "envio", source = "shipment")
    @Mapping(target = "detalles", source = "lines")
    DespatchAdvice map(XMLDespatchAdvice xml);

    @Mapping(target = "tipoDocumento", source = "orderTypeCode")
    @Mapping(target = "serieNumero", source = "id")
    DocumentoBaja mapDocumentoBaja(XMLDespatchAdvice.OrderReference xml);

    @Mapping(target = "tipoDocumento", source = "documentTypeCode")
    @Mapping(target = "serieNumero", source = "id")
    DocumentoRelacionado mapDocumentoRelacionado(XMLDespatchAdvice.AdditionalDocumentReference xml);

    @Mapping(target = "ruc", source = "party.partyIdentification.id.value")
    @Mapping(target = "razonSocial", source = "party.partyLegalEntity.registrationName")
    Remitente mapRemitente(XMLDespatchAdvice.DespatchSupplierParty xml);

    @Mapping(target = "numeroDocumentoIdentidad", source = "party.partyIdentification.id.value")
    @Mapping(target = "tipoDocumentoIdentidad", source = "party.partyIdentification.id.schemeID")
    @Mapping(target = "nombre", source = "party.partyLegalEntity.registrationName")
    Destinatario mapDestinatario(XMLDespatchAdvice.DeliveryCustomerParty xml);

    @Mapping(target = "ruc", source = "customerAssignedAccountId")
    @Mapping(target = "nombreComercial", source = "party.partyLegalEntity.registrationName")
    Proveedor mapProveedor(XMLDespatchAdvice.SellerSupplierParty xml);

    @Mapping(target = "tipoTraslado", source = "handlingCode")
    @Mapping(target = "motivoTraslado", source = "information")
    @Mapping(target = "pesoTotal", source = "grossWeightMeasure.value")
    @Mapping(target = "pesoTotalUnidadMedida", source = "grossWeightMeasure.unitCode")
    @Mapping(target = "numeroDeBultos", source = "totalTransportHandlingUnitQuantity")
    @Mapping(target = "transbordoProgramado", source = "splitConsignmentIndicator")
    @Mapping(target = "tipoModalidadTraslado", source = "shipmentStage.transportModeCode")
    @Mapping(target = "fechaTraslado", source = "shipmentStage.transitPeriod.startDate")
    @Mapping(target = "numeroDeContenedor", source = "transportHandlingUnit.transportEquipment.id")
    @Mapping(target = "codigoDePuerto", source = "firstArrivalPortLocation.id")
    @Mapping(target = "transportista", source = "shipmentStage", conditionQualifiedByName = "transportistaRequirements")
    @Mapping(target = "destino", source = "delivery")
    @Mapping(target = "partida", source = "originAddress")
    Envio mapEnvio(XMLDespatchAdvice.Shipment xml);

    @Condition
    @Named("transportistaRequirements")
    default boolean conditionTransportista(XMLDespatchAdvice.ShipmentStage xml) {
        return xml.getCarrierParty() != null && xml.getTransportMeans() != null && xml.getDriverPerson() != null;
    }

    @Mapping(target = "tipoDocumentoIdentidad", source = "carrierParty.partyIdentification.id.value")
    @Mapping(target = "numeroDocumentoIdentidad", source = "carrierParty.partyIdentification.id.schemeID")
    @Mapping(target = "nombre", source = "carrierParty.partyName.name")
    @Mapping(target = "placaDelVehiculo", source = "transportMeans.roadTransport.licensePlateID")
    @Mapping(target = "choferTipoDocumentoIdentidad", source = "driverPerson.id.schemeID")
    @Mapping(target = "choferNumeroDocumentoIdentidad", source = "driverPerson.id.value")
    Transportista mapTransportista(XMLDespatchAdvice.ShipmentStage xml);

    @Mapping(target = "ubigeo", source = "deliveryAddress.id")
    @Mapping(target = "direccion", source = "deliveryAddress.addressLine.line")
    Destino mapDelivery(XMLDespatchAdvice.Delivery xml);

    @Mapping(target = "ubigeo", source = "id")
    @Mapping(target = "direccion", source = "streetName")
    Partida mapPartida(XMLDespatchAdvice.OriginAddress xml);

    @Mapping(target = "unidadMedida", source = "deliveredQuantity.unitCode")
    @Mapping(target = "cantidad", source = "deliveredQuantity.value")
    @Mapping(target = "descripcion", source = "item.name")
    @Mapping(target = "codigo", source = "item.sellersItemIdentification.id")
    @Mapping(target = "codigoSunat", source = "item.commodityClassification.itemClassificationCode")
    DespatchAdviceItem mapLine(XMLDespatchAdviceLine xml);
}
