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
package io.github.project.openubl.quarkus.xbuilder.deployment;

import io.github.project.openubl.quarkus.xbuilder.XBuilder;
import io.github.project.openubl.quarkus.xbuilder.runtime.CustomTemplateLocator;
import io.github.project.openubl.quarkus.xbuilder.runtime.DefaultXBuilder;
import io.github.project.openubl.xbuilder.enricher.kie.RuleFactory;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;
import io.quarkus.deployment.util.ServiceUtil;
import io.quarkus.jaxb.deployment.JaxbClassesToBeBoundBuildItem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class QuarkusXbuilderProcessor {

    private static final String FEATURE = "quarkus-xbuilder";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(XBuilder.class, DefaultXBuilder.class, CustomTemplateLocator.class)
                .build();
    }

    @BuildStep
    void registerTemplates(BuildProducer<NativeImageResourceBuildItem> resource) throws URISyntaxException {
        resource.produce(
                new NativeImageResourceBuildItem(
                        "templates/Renderer/creditNote.xml",
                        "templates/Renderer/debitNote.xml",
                        "templates/Renderer/invoice.xml",
                        "templates/Renderer/voidedDocuments.xml",
                        "templates/Renderer/summaryDocuments.xml",
                        "templates/Renderer/perception.xml",
                        "templates/Renderer/retention.xml",
                        "templates/Renderer/despatchAdvice.xml",

                        "templates/ubl/common/signature.xml",

                        "templates/ubl/standard/include/address.xml",
                        "templates/ubl/standard/include/contact.xml",
                        "templates/ubl/standard/include/customer.xml",
                        "templates/ubl/standard/include/document-line.xml",
                        "templates/ubl/standard/include/documentos-relacionados.xml",
                        "templates/ubl/standard/include/general-data.xml",
                        "templates/ubl/standard/include/guias.xml",
                        "templates/ubl/standard/include/monetary-total.xml",
                        "templates/ubl/standard/include/namespaces.xml",
                        "templates/ubl/standard/include/note/invoice-reference.xml",
                        "templates/ubl/standard/include/payment-terms.xml",
                        "templates/ubl/standard/include/supplier.xml",
                        "templates/ubl/standard/include/tax-total.xml",
                        "templates/ubl/standard/include/ubl-extensions.xml",

                        "templates/ubl/sunat/include/supplier.xml",
                        "templates/ubl/sunat/include/agent-party.xml",
                        "templates/ubl/sunat/include/receiver-party.xml"
                )
        );
        //        resource.produce(new NativeImageResourceDirectoryBuildItem("templates"));
    }

    @BuildStep
    void registerServices(BuildProducer<ServiceProviderBuildItem> services) throws IOException {
        String service = "META-INF/services/" + RuleFactory.class.getName();

        // find out all the implementation classes listed in the service files
        Set<String> implementations = ServiceUtil.classNamesNamedIn(
                Thread.currentThread().getContextClassLoader(),
                service
        );

        // register every listed implementation class so they can be instantiated
        // in native-image at run-time
        services.produce(
                new ServiceProviderBuildItem(RuleFactory.class.getName(), implementations.toArray(new String[0]))
        );
    }

    @BuildStep
    ReflectiveClassBuildItem reflectionModelsLombok() {
        return new ReflectiveClassBuildItem(true, false,
                "io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote$CreditNoteBuilderImpl",
                "io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote$DebitNoteBuilderImpl",
                "io.github.project.openubl.xbuilder.content.models.standard.general.Invoice$InvoiceBuilderImpl",

                "io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocuments$VoidedDocumentsBuilderImpl",
                "io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocuments$SummaryDocumentsBuilderImpl",

                "io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Perception$PerceptionBuilderImpl",
                "io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Retention$RetentionBuilderImpl"
        );
    }

    @BuildStep
    ReflectiveClassBuildItem reflectionJaxbLombok() {
        return new ReflectiveClassBuildItem(true, true,
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLAddress$AddressLine",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLAddress$Country",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLCreditNoteLine$Quantity",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLCustomer$PartyIdentification",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLCustomer$PartyIdentification_ID",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLCustomer$PartyLegalEntity",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDebitNoteLine$Quantity",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$AdditionalDocumentReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$AddressLine",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$CarrierParty",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$Delivery",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$DeliveryAddress",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$DeliveryCustomerParty",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$DespatchSupplierParty",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$DriverPerson",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$FirstArrivalPortLocation",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$GrossWeightMeasure",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$ID",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$OrderReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$OriginAddress",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$Party",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$PartyIdentification",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$PartyLegalEntity",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$PartyName",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$RoadTransport",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$SellerSupplierParty",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$Shipment",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$ShipmentStage",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$TransitPeriod",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$TransportEquipment",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$TransportHandlingUnit",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice$TransportMeans",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdviceLine$CommodityClassification",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdviceLine$DeliveredQuantity",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdviceLine$Item",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdviceLine$SellersItemIdentification",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoiceLine$Quantity",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionRetencionBase$TotalInvoiceAmount",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionRetencionSunatDocumentReferenceBase$ID",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionRetencionSunatDocumentReferenceBase$Payment",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionRetencionSunatDocumentReferenceBase$TotalInvoiceAmount",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionSunatDocumentReference$ExchangeRate",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionSunatDocumentReference$XMLPercepcionInformation",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLRetentionSunatDocumentReference$ExchangeRate",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLRetentionSunatDocumentReference$XMLRetentionInformation",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$AccountingCustomerParty",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$AccountingSupplierParty",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$AdditionalDocumentReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$AllowanceCharge",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$Delivery",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$DeliveryLocation",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$DespatchDocumentReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$DiscrepancyResponse",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$InvoiceTypeCode",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$MonetaryTotal",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$Note",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$OrderReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$PayeeFinancialAccount",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$PaymentMeans",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$PaymentTerms",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$PrepaidPayment",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$TaxCategory",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$TaxScheme",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$TaxSubtotal",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument$TaxTotal",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$AlternativeConditionPrice",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$Item",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$Price",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$PricingReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$TaxCategory",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$TaxScheme",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$TaxSubtotalLine",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine$TaxTotalLine",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSignature$PartyName",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSignature$SignatoryParty",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$AccountingCustomerParty",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$AllowanceCharge",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$BillingPayment",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$BillingReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$InvoiceDocumentReference",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$Status",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$TaxCategory",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$TaxScheme",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$TaxSubtotal",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$TaxTotal",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine$TotalAmount",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplier$PartyIdentification",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplier$PartyLegalEntity",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplier$PartyName",

                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplierSunat$Party",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplierSunat$PartyLegalEntity",
                "io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplierSunat$PartyName"
        );
    }

    @BuildStep
    ReflectiveClassBuildItem reflectionModels() {
        return new ReflectiveClassBuildItem(true, false,
                io.github.project.openubl.xbuilder.content.models.common.Cliente.class,
                io.github.project.openubl.xbuilder.content.models.common.Cliente.ClienteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.common.Contacto.class,
                io.github.project.openubl.xbuilder.content.models.common.Contacto.ContactoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.common.Direccion.class,
                io.github.project.openubl.xbuilder.content.models.common.Direccion.DireccionBuilder.class,
                io.github.project.openubl.xbuilder.content.models.common.Document.class,
                io.github.project.openubl.xbuilder.content.models.common.Document.DocumentBuilder.class,
                io.github.project.openubl.xbuilder.content.models.common.Firmante.class,
                io.github.project.openubl.xbuilder.content.models.common.Firmante.FirmanteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.common.Proveedor.class,
                io.github.project.openubl.xbuilder.content.models.common.Proveedor.ProveedorBuilder.class,
                io.github.project.openubl.xbuilder.content.models.common.TipoCambio.class,
                io.github.project.openubl.xbuilder.content.models.common.TipoCambio.TipoCambioBuilder.class,

                io.github.project.openubl.xbuilder.content.models.standard.general.Anticipo.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Anticipo.AnticipoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Descuento.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Descuento.DescuentoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.BaseDocumentoTributarioRelacionado.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.BaseDocumentoTributarioRelacionado.BaseDocumentoTributarioRelacionadoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.CargoDescuento.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.CargoDescuento.CargoDescuentoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote.CreditNoteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.CuotaDePago.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.CuotaDePago.CuotaDePagoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote.DebitNoteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Detraccion.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Detraccion.DetraccionBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoRelacionado.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoRelacionado.DocumentoRelacionadoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle.DocumentoVentaDetalleBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.FormaDePago.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.FormaDePago.FormaDePagoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Guia.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Guia.GuiaBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Invoice.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Invoice.InvoiceBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Note.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Note.NoteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Percepcion.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.Percepcion.PercepcionBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument.SalesDocumentBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporte.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporte.TotalImporteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteInvoice.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteInvoice.TotalImporteInvoiceBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteNote.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteNote.TotalImporteNoteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImpuestos.class,
                io.github.project.openubl.xbuilder.content.models.standard.general.TotalImpuestos.TotalImpuestosBuilder.class,

                io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice.DespatchAdviceBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdviceItem.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdviceItem.DespatchAdviceItemBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Destinatario.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Destinatario.DestinatarioBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Destino.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Destino.DestinoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoBaja.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoBaja.DocumentoBajaBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoRelacionado.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.DocumentoRelacionado.DocumentoRelacionadoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Envio.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Envio.EnvioBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Partida.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Partida.PartidaBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Proveedor.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Proveedor.ProveedorBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Remitente.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Remitente.RemitenteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Transportista.class,
                io.github.project.openubl.xbuilder.content.models.standard.guia.Transportista.TransportistaBuilder.class,

                io.github.project.openubl.xbuilder.content.catalogs.Catalog.class,
                io.github.project.openubl.xbuilder.content.catalogs.CatalogContadoCredito.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog1.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Guia.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Invoice.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog5.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog6.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog7.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog7_1.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog8.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog9.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog10.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog12.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog12_Anticipo.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog12_Doc_Trib_Relacionado_BoletaFactura.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog12_Doc_Trib_Relacionado_NotaCredito.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog12_Doc_Trib_Relacionado_NotaDebito.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog16.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog18.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog19.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog20.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog21.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog22.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog23.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog51.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog52.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog53.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog53_Anticipo.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog53_DescuentoGlobal.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog54.class,
                io.github.project.openubl.xbuilder.content.catalogs.Catalog59.class,

                io.github.project.openubl.xbuilder.content.models.sunat.SunatDocument.class,
                io.github.project.openubl.xbuilder.content.models.sunat.SunatDocument.SunatDocumentBuilder.class,

                io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocuments.class,
                io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocuments.VoidedDocumentsBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocumentsItem.class,
                io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocumentsItem.VoidedDocumentsItemBuilder.class,

                io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocuments.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocuments.SummaryDocumentsBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocumentsItem.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocumentsItem.SummaryDocumentsItemBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.Comprobante.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.Comprobante.ComprobanteBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteAfectado.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteAfectado.ComprobanteAfectadoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteImpuestos.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteImpuestos.ComprobanteImpuestosBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteValorVenta.class,
                io.github.project.openubl.xbuilder.content.models.sunat.resumen.ComprobanteValorVenta.ComprobanteValorVentaBuilder.class,

                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Perception.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Perception.PerceptionBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Retention.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Retention.RetentionBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.BasePercepcionRetencion.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.BasePercepcionRetencion.BasePercepcionRetencionBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.ComprobanteAfectado.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.ComprobanteAfectado.ComprobanteAfectadoBuilder.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.PercepcionRetencionOperacion.class,
                io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.PercepcionRetencionOperacion.PercepcionRetencionOperacionBuilder.class
        );
    }

    @BuildStep
    ReflectiveClassBuildItem reflectionJaxb() {
        return new ReflectiveClassBuildItem(true, true,
                io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalDateAdapter.class,
                io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalTimeAdapter.class,

                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ClienteMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ContactoMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.DireccionMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.FirmanteMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.GuiaMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.Numero2Translator.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.Numero3Translator.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ProveedorMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentHelperMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieNumeroTranslator.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SerieTranslator.class,

                io.github.project.openubl.xbuilder.content.jaxb.mappers.utils.MapperUtils.class,

                io.github.project.openubl.xbuilder.content.jaxb.mappers.CreditNoteMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.DebitNoteMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.DespatchAdviceMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.InvoiceMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.PerceptionMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.RetentionMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.SummaryDocumentsMapper.class,
                io.github.project.openubl.xbuilder.content.jaxb.mappers.VoidedDocumentsMapper.class,

                io.github.project.openubl.xbuilder.content.jaxb.models.XMLAddress.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLConstants.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLContact.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLCreditNote.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLCreditNoteLine.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLCustomer.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLDebitNote.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLDebitNoteLine.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdviceLine.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoice.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoiceLine.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcion.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionRetencionBase.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionRetencionSunatDocumentReferenceBase.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcionSunatDocumentReference.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLRetention.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLRetentionSunatDocumentReference.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSignature.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocuments.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocumentsLine.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSunatDocument.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplier.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplierSunat.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLVoidedDocuments.class,
                io.github.project.openubl.xbuilder.content.jaxb.models.XMLVoidedDocumentsLine.class
        );
    }

    @BuildStep
    ReflectiveClassBuildItem mapstruct() {
        return new ReflectiveClassBuildItem(true, false,
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ClienteMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ContactoMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.common.DireccionMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.common.FirmanteMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.common.GuiaMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.common.ProveedorMapperImpl",

                "io.github.project.openubl.xbuilder.content.jaxb.mappers.CreditNoteMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.DebitNoteMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.DespatchAdviceMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.InvoiceMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.PerceptionMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.RetentionMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.SummaryDocumentsMapperImpl",
                "io.github.project.openubl.xbuilder.content.jaxb.mappers.VoidedDocumentsMapperImpl"
        );
    }

    @BuildStep
    void jaxbRegisterClassesToBeBound(BuildProducer<JaxbClassesToBeBoundBuildItem> classesToBeBoundBuildItemProducer) {
        List<String> classesToBeBound = new ArrayList<>();
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoice.class.getName());
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLCreditNote.class.getName());
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLDebitNote.class.getName());
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice.class.getName());
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLVoidedDocuments.class.getName());
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocuments.class.getName());
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcion.class.getName());
        classesToBeBound.add(io.github.project.openubl.xbuilder.content.jaxb.models.XMLRetention.class.getName());

        classesToBeBoundBuildItemProducer.produce(new JaxbClassesToBeBoundBuildItem(classesToBeBound));
    }
}
