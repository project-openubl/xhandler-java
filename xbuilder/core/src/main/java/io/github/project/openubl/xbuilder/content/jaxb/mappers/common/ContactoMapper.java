package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.jaxb.models.XMLContact;
import io.github.project.openubl.xbuilder.content.models.common.Contacto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ContactoMapper {

    @Mapping(target = "telefono", source = "telephone")
    @Mapping(target = "email", source = "electronicMail")
    Contacto map(XMLContact xml);

}
