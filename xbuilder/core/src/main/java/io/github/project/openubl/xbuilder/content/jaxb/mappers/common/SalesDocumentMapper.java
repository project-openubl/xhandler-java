package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument;
import io.github.project.openubl.xbuilder.content.models.standard.general.Guia;
import io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

@MapperConfig(
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
        uses = {
                SerieNumeroMapper.class,
                FirmanteMapper.class,
                ProveedorMapper.class,
                ClienteMapper.class,
                GuiaMapper.class
        }
)
public interface SalesDocumentMapper {

    @Mapping(target = "serie", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, SerieTranslator.class})
    @Mapping(target = "numero", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, Numero2Translator.class})

    @Mapping(target = "moneda", source = "documentCurrencyCode")
    @Mapping(target = "fechaEmision", source = "issueDate")
    @Mapping(target = "horaEmision", source = "issueTime")
    @Mapping(target = "ordenDeCompra", source = "orderReference.id")

    @Mapping(target = "firmante", source = "signature")
    @Mapping(target = "proveedor", source = "accountingSupplierParty.party")
    @Mapping(target = "cliente", source = "accountingCustomerParty.party")

    @Mapping(target = "leyendas", source = "notes")
    @Mapping(target = "guias", source = "despatchDocumentReferences")
    @Mapping(target = "totalImpuestos", source = "taxTotal")
    @Mapping(target = "documentosRelacionados", source = "additionalDocumentReferences")
    SalesDocument map(XMLSalesDocument xml);

}
