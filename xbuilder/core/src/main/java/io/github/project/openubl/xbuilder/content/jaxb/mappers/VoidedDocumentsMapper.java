/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
