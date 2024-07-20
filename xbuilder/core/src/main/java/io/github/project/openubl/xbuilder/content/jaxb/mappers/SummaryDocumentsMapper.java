package io.github.project.openubl.xbuilder.content.jaxb.mappers;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog5;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.FirmanteMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.Numero3Translator;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ProveedorMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroTranslator;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocuments;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.Comprobante;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteImpuestos;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteValorVenta;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocuments;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocumentsItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(uses = {
        SerieNumeroMapper.class,
        FirmanteMapper.class,
        ProveedorMapper.class
})
public interface SummaryDocumentsMapper {

    @Mapping(target = "fechaEmision", source = "issueDate")
    @Mapping(target = "firmante", source = "signature")
    @Mapping(target = "proveedor", source = "accountingSupplierParty")

    @Mapping(target = "numero", source = "documentId", qualifiedBy = {SerieNumeroTranslator.class, Numero3Translator.class})
    @Mapping(target = "fechaEmisionComprobantes", source = "referenceDate")
    @Mapping(target = "comprobantes", source = "lines")
    SummaryDocuments map(XMLSummaryDocuments xml);

    @Mapping(target = "tipoOperacion", source = "status.conditionCode")
    @Mapping(target = "comprobante", source = ".")
    SummaryDocumentsItem mapLines(XMLSummaryDocumentsLine xml);

    @Mapping(target = "moneda", source = "totalAmount.currencyID")
    @Mapping(target = "tipoComprobante", source = "documentTypeCode")
    @Mapping(target = "serieNumero", source = "documentId")
    @Mapping(target = "cliente", source = "accountingCustomerParty")
    @Mapping(target = "comprobanteAfectado.serieNumero", source = "billingReference.invoiceDocumentReference.id")
    @Mapping(target = "comprobanteAfectado.tipoComprobante", source = "billingReference.invoiceDocumentReference.documentTypeCode")
    @Mapping(target = "valorVenta", source = ".")
    @Mapping(target = "impuestos", source = ".")
    Comprobante mapLineComprobante(XMLSummaryDocumentsLine xml);

    @Mapping(target = "numeroDocumentoIdentidad", source = "customerAssignedAccountID")
    @Mapping(target = "tipoDocumentoIdentidad", source = "additionalAccountID")
    Cliente mapCliente(XMLSummaryDocumentsLine.AccountingCustomerParty xml);

    default ComprobanteValorVenta mapLineComprobanteValorVenta(XMLSummaryDocumentsLine xml) {
        if (xml == null) {
            return null;
        }

        Map<String, BigDecimal> billingPayments = Optional.ofNullable(xml.getBillingPayments())
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(
                        XMLSummaryDocumentsLine.BillingPayment::getInstructionId,
                        XMLSummaryDocumentsLine.BillingPayment::getPaidAmount
                ));

        BigDecimal importeTotal = Optional.ofNullable(xml.getTotalAmount())
                .map(XMLSummaryDocumentsLine.TotalAmount::getValue)
                .orElse(null);
        BigDecimal otrosCargos = Optional.ofNullable(xml.getAllowanceCharge())
                .map(XMLSummaryDocumentsLine.AllowanceCharge::getValue)
                .orElse(null);

        return ComprobanteValorVenta.builder()
                .importeTotal(importeTotal)
                .gravado(billingPayments.get("01"))
                .exonerado(billingPayments.get("02"))
                .inafecto(billingPayments.get("03"))
                .gratuito(billingPayments.get("05"))
                .otrosCargos(otrosCargos)
                .build();
    }

    default ComprobanteImpuestos mapLineComprobanteImpuestos(XMLSummaryDocumentsLine xml) {
        if (xml == null) {
            return null;
        }

        Map<Catalog5, BigDecimal> taxTotals = Optional.ofNullable(xml.getTaxTotals())
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(
                        taxTotal -> Optional.ofNullable(taxTotal.getTaxSubtotals())
                                .flatMap(f -> Optional.ofNullable(f.getTaxCategory()))
                                .flatMap(f -> Optional.ofNullable(f.getTaxScheme()))
                                .flatMap(taxScheme -> Optional.ofNullable(taxScheme.getId()))
                                .flatMap(code -> Catalog.valueOfCode(Catalog5.class, code))
                                .orElse(null),
                        taxTotal -> Optional.ofNullable(taxTotal.getTaxAmount()).orElse(BigDecimal.ZERO)
                ));

        return ComprobanteImpuestos.builder()
                .igv(taxTotals.get(Catalog5.IGV))
                .icb(taxTotals.get(Catalog5.ICBPER))
                .build();
    }
}
