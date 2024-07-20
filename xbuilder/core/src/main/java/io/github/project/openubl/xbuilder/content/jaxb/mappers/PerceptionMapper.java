package io.github.project.openubl.xbuilder.content.jaxb.mappers;

import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ClienteMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.FirmanteMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.Numero2Translator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ProveedorMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcion;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionSunatDocumentReference;
import io.github.project.openubl.xbuilder.content.models.common.TipoCambio;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.ComprobanteAfectado;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.PercepcionRetencionOperacion;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Perception;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        SerieNumeroMapper.class,
        ProveedorMapper.class,
        ClienteMapper.class,
        FirmanteMapper.class
})
public interface PerceptionMapper {

    @Mapping(target = "serie", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, SerieTranslator.class})
    @Mapping(target = "numero", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, Numero2Translator.class})
    @Mapping(target = "tipoRegimen", source = "sunatSystemCode")
    @Mapping(target = "importeTotalPercibido", source = "sunatDocumentReference.sunatInformation.sunatAmount")
    @Mapping(target = "importeTotalCobrado", source = "sunatDocumentReference.sunatInformation.sunatNetTotal")
    // General Document
    @Mapping(target = "moneda", source = "totalInvoiceAmount.currencyID")
    @Mapping(target = "fechaEmision", source = "issueDate")
    @Mapping(target = "proveedor", source = "accountingSupplierParty")
    @Mapping(target = "firmante", source = "signature")
    // PerceptionRetention General
    @Mapping(target = "tipoRegimenPorcentaje", source = "sunatPercent")
    @Mapping(target = "observacion", source = "note")
    @Mapping(target = "cliente", source = "accountingCustomerParty")
    @Mapping(target = "operacion", source = "sunatDocumentReference")
    Perception map(XMLPercepcion xml);

    @Mapping(target = "numeroOperacion", source = "payment.id")
    @Mapping(target = "fechaOperacion", source = "payment.paidDate")
    @Mapping(target = "importeOperacion", source = "payment.paidAmount")
    @Mapping(target = "comprobante", source = ".")
    @Mapping(target = "tipoCambio", source = "sunatInformation.exchangeRate")
    PercepcionRetencionOperacion map(XMLPercepcionSunatDocumentReference xml);

    @Mapping(target = "moneda", source = "totalInvoiceAmount.currencyID")
    @Mapping(target = "importeTotal", source = "totalInvoiceAmount.value")
    @Mapping(target = "serieNumero", source = "id.value")
    @Mapping(target = "tipoComprobante", source = "id.schemeID")
    @Mapping(target = "fechaEmision", source = "issueDate")
    ComprobanteAfectado mapComprobante(XMLPercepcionSunatDocumentReference xml);

    @Mapping(target = "fecha", source = "date")
    @Mapping(target = "valor", source = "calculationRate")
    TipoCambio mapTipoCambio(XMLPercepcionSunatDocumentReference.ExchangeRate xml);
}
