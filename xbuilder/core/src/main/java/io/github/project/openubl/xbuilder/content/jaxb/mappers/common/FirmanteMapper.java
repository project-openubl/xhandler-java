package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSignature;
import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        ContactoMapper.class,
        DireccionMapper.class
})
public interface FirmanteMapper {

    @Mapping(target = "ruc", source = "id")
    @Mapping(target = "razonSocial", source = "signatoryParty.partyName.name")
    Firmante map(XMLSignature xml);

}
