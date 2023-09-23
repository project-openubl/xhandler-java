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

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53_Anticipo;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53_DescuentoGlobal;
import io.github.project.openubl.xbuilder.content.catalogs.CatalogContadoCredito;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.DireccionMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentHelperMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentMapper;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoice;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoiceLine;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument;
import io.github.project.openubl.xbuilder.content.models.standard.general.Anticipo;
import io.github.project.openubl.xbuilder.content.models.standard.general.CuotaDePago;
import io.github.project.openubl.xbuilder.content.models.standard.general.Descuento;
import io.github.project.openubl.xbuilder.content.models.standard.general.Detraccion;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.FormaDePago;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.general.Percepcion;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteInvoice;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.project.openubl.xbuilder.content.jaxb.mappers.utils.MapperUtils.mapPorcentaje;

@Mapper(config = SalesDocumentMapper.class,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        uses = {
                DireccionMapper.class
        }
)
public interface InvoiceMapper extends SalesDocumentHelperMapper {

    @Mapping(target = "fechaVencimiento", source = "dueDate")
    @Mapping(target = "tipoComprobante", source = "invoiceTypeCode.value")
    @Mapping(target = "observaciones", source = "notes")
    @Mapping(target = "tipoOperacion", source = "invoiceTypeCode.listID")
    @Mapping(target = "formaDePago", source = "paymentTerms")

    @Mapping(target = "totalImporte", source = "monetaryTotal")
    @Mapping(target = "direccionEntrega", source = "delivery.deliveryLocation.address")
    @Mapping(target = "detraccion", source = ".")
    @Mapping(target = "percepcion", source = ".")
    @Mapping(target = "anticipos", source = ".")
    @Mapping(target = "descuentos", source = "allowanceCharges")

    @Mapping(target = "detalles", source = "lines")
    Invoice map(XMLInvoice xml);

    @Mapping(target = "importe", source = "payableAmount")
    @Mapping(target = "importeSinImpuestos", source = "lineExtensionAmount")
    @Mapping(target = "importeConImpuestos", source = "taxInclusiveAmount")
    @Mapping(target = "anticipos", source = "prepaidAmount")
    @Mapping(target = "descuentos", source = "allowanceTotalAmount")
    TotalImporteInvoice mapTotalImporteInvoice(XMLSalesDocument.MonetaryTotal xml);

    default String mapObservaciones(List<XMLSalesDocument.Note> xml) {
        if (xml == null) {
            return null;
        }

        return xml.stream()
                .filter(e -> e.getLanguageLocaleId() == null)
                .findFirst()
                .map(XMLSalesDocument.Note::getValue)
                .orElse(null);
    }

    default FormaDePago mapFormaDePago(List<XMLSalesDocument.PaymentTerms> paymentTerms) {
        if (paymentTerms == null) {
            return null;
        }

        List<XMLSalesDocument.PaymentTerms> formasDePago = paymentTerms.stream()
                .filter(elem -> Objects.equals(elem.getId(), "FormaPago"))
                .collect(Collectors.toList());
        if (formasDePago.isEmpty()) {
            return null;
        }

        XMLSalesDocument.PaymentTerms formaDePago = formasDePago.stream()
                .filter(elem -> Objects.equals(elem.getPaymentMeansID(), CatalogContadoCredito.CREDITO.getCode()) ||
                        Objects.equals(elem.getPaymentMeansID(), CatalogContadoCredito.CONTADO.getCode())
                )
                .findFirst()
                .orElse(new XMLSalesDocument.PaymentTerms());

        List<XMLSalesDocument.PaymentTerms> cuotas = formasDePago.stream()
                .filter(elem -> elem.getPaymentMeansID().startsWith("Cuota"))
                .collect(Collectors.toList());

        return FormaDePago.builder()
                .tipo(formaDePago.getPaymentMeansID())
                .total(formaDePago.getAmount())
                .cuotas(cuotas.stream()
                        .map(elem -> CuotaDePago.builder()
                                .importe(elem.getAmount())
                                .fechaPago(elem.getPaymentDueDate())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    default Detraccion mapDetraccion(XMLSalesDocument xml) {
        if (xml == null) {
            return null;
        }

        XMLSalesDocument.PaymentMeans paymentMeans = xml.getPaymentMeans();
        List<XMLSalesDocument.PaymentTerms> paymentTerms = xml.getPaymentTerms();
        if (paymentMeans == null || paymentTerms == null) {
            return null;
        }

        String cuentaBancaria = Optional.ofNullable(paymentMeans.getPayeeFinancialAccount())
                .map(XMLSalesDocument.PayeeFinancialAccount::getId)
                .orElse(null);

        Detraccion.DetraccionBuilder builder = Detraccion.builder()
                .medioDePago(paymentMeans.getPaymentMeansCode())
                .cuentaBancaria(cuentaBancaria);

        paymentTerms.stream()
                .filter(elem -> Objects.equals(elem.getId(), "Detraccion"))
                .findFirst()
                .ifPresent(el -> builder
                        .tipoBienDetraido(el.getPaymentMeansID())
                        .porcentaje(mapPorcentaje(el.getPaymentPercent()))
                        .monto(el.getAmount())
                );

        return builder.build();
    }

    default Percepcion mapPercepcion(XMLSalesDocument xml) {
        if (xml == null) {
            return null;
        }

        List<XMLSalesDocument.PaymentTerms> paymentTerms = xml.getPaymentTerms();
        List<XMLSalesDocument.AllowanceCharge> allowanceCharges = xml.getAllowanceCharges();
        if (paymentTerms == null || allowanceCharges == null) {
            return null;
        }

        Optional<XMLSalesDocument.PaymentTerms> paymentTerm = paymentTerms.stream()
                .filter(elem -> Objects.equals(elem.getId(), "Percepcion"))
                .findFirst();
        Optional<XMLSalesDocument.AllowanceCharge> allowanceCharge = allowanceCharges.stream()
                .filter(XMLSalesDocument.AllowanceCharge::getChargeIndicator)
                .findFirst();

        if (paymentTerm.isEmpty() || allowanceCharge.isEmpty()) {
            return null;
        }

        Percepcion.PercepcionBuilder builder = Percepcion.builder();
        paymentTerm.ifPresent(elem -> builder
                .montoTotal(elem.getAmount())
        );
        allowanceCharge.ifPresent(elem -> builder
                .tipo(elem.getAllowanceChargeReasonCode())
                .porcentaje(elem.getMultiplierFactorNumeric())
                .monto(elem.getAmount())
                .montoBase(elem.getBaseAmount())
        );

        return builder.build();
    }

    default List<Anticipo> mapAnticipos(XMLSalesDocument xml) {
        if (xml == null) {
            return null;
        }

        List<XMLSalesDocument.AdditionalDocumentReference> additionalDocumentReferences = xml.getAdditionalDocumentReferences();
        List<XMLSalesDocument.PrepaidPayment> prepaidPayments = xml.getPrepaidPayments();
        List<XMLSalesDocument.AllowanceCharge> allowanceCharges = xml.getAllowanceCharges();
        if (additionalDocumentReferences == null || prepaidPayments == null || allowanceCharges == null) {
            return Collections.emptyList();
        }

        additionalDocumentReferences = additionalDocumentReferences.stream()
                .filter(additionalDocumentReference -> additionalDocumentReference.getDocumentStatusCode() != null)
                .collect(Collectors.toList());

        allowanceCharges = allowanceCharges.stream()
                .filter(allowanceCharge -> !allowanceCharge.getChargeIndicator())
                .filter(allowanceCharge -> Catalog
                        .valueOfCode(Catalog53_Anticipo.class, allowanceCharge.getAllowanceChargeReasonCode())
                        .isPresent()
                )
                .collect(Collectors.toList());

        if (additionalDocumentReferences.size() != prepaidPayments.size() || additionalDocumentReferences.size() != allowanceCharges.size()) {
            return Collections.emptyList();
        }

        List<Anticipo> result = new ArrayList<>();
        for (int i = 0; i < additionalDocumentReferences.size(); i++) {
            XMLSalesDocument.AdditionalDocumentReference additionalDocumentReference = additionalDocumentReferences.get(i);
            XMLSalesDocument.PrepaidPayment prepaidPayment = prepaidPayments.get(i);
            XMLSalesDocument.AllowanceCharge allowanceCharge = allowanceCharges.get(i);

            result.add(Anticipo.builder()
                    .comprobanteSerieNumero(additionalDocumentReference.getId())
                    .comprobanteTipo(additionalDocumentReference.getDocumentTypeCode())
                    .monto(prepaidPayment.getPaidAmount())
                    .tipo(allowanceCharge.getAllowanceChargeReasonCode())
                    .build()
            );
        }

        return result;
    }

    default List<Descuento> mapDescuentos(List<XMLSalesDocument.AllowanceCharge> xml) {
        if (xml == null) {
            return Collections.emptyList();
        }

        return xml.stream()
                .filter(allowanceCharge -> !allowanceCharge.getChargeIndicator())
                .filter(allowanceCharge -> Catalog
                        .valueOfCode(Catalog53_DescuentoGlobal.class, allowanceCharge.getAllowanceChargeReasonCode())
                        .isPresent()
                )
                .map(allowanceCharge -> Descuento.builder()
                        .tipoDescuento(allowanceCharge.getAllowanceChargeReasonCode())
                        .factor(allowanceCharge.getMultiplierFactorNumeric())
                        .monto(allowanceCharge.getAmount())
                        .montoBase(allowanceCharge.getBaseAmount())
                        .build()
                )
                .collect(Collectors.toList());
    }

    default DocumentoVentaDetalle mapDocumentoVentaDetalle(XMLInvoiceLine xml) {
        DocumentoVentaDetalle documentoVentaDetalle = mapSalesDocumentDetalle(xml);
        documentoVentaDetalle.setCantidad(xml.getQuantity().getValue());
        documentoVentaDetalle.setUnidadMedida(xml.getQuantity().getUnitCode());

        return documentoVentaDetalle;
    }
}
