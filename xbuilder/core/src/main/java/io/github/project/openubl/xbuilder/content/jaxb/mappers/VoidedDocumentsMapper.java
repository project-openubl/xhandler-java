package io.github.project.openubl.xbuilder.content.jaxb.mappers;

import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.FirmanteMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.Numero3Translator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ProveedorMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLVoidedDocuments;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLVoidedDocumentsLine;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocuments;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocumentsItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        SerieNumeroMapper.class,
        FirmanteMapper.class,
        ProveedorMapper.class
})
public interface VoidedDocumentsMapper {

    @Mapping(target = "fechaEmision", source = "issueDate")
    @Mapping(target = "firmante", source = "signature")
    @Mapping(target = "proveedor", source = "accountingSupplierParty")

    @Mapping(target = "numero", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, Numero3Translator.class})
    @Mapping(target = "fechaEmisionComprobantes", source = "referenceDate")
    @Mapping(target = "comprobantes", source = "lines")
    VoidedDocuments map(XMLVoidedDocuments xml);

    @Mapping(target = "tipoComprobante", source = "documentTypeCode")
    @Mapping(target = "serie", source = "documentSerialID")
    @Mapping(target = "numero", source = "documentNumberID")
    @Mapping(target = "descripcionSustento", source = "voidReasonDescription")
    VoidedDocumentsItem mapLines(XMLVoidedDocumentsLine xml);
}
