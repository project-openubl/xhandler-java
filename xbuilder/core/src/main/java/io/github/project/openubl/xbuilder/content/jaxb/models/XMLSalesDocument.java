package io.github.project.openubl.xbuilder.content.jaxb.models;

import io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalDateAdapter;
import io.github.project.openubl.xbuilder.content.jaxb.adapters.LocalTimeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@XmlType(name = "SalesDocument")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public abstract class XMLSalesDocument {

    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private String documentId;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "IssueDate", namespace = XMLConstants.CBC)
    private LocalDate issueDate;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    @XmlElement(name = "IssueTime", namespace = XMLConstants.CBC)
    private LocalTime issueTime;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "DueDate", namespace = XMLConstants.CBC)
    private LocalDate dueDate;

    @XmlElement(name = "InvoiceTypeCode", namespace = XMLConstants.CBC)
    private InvoiceTypeCode invoiceTypeCode;

    @XmlElement(name = "Note", namespace = XMLConstants.CBC)
    private List<Note> notes;

    @XmlElement(name = "DocumentCurrencyCode", namespace = XMLConstants.CBC)
    private String documentCurrencyCode;

    @XmlElement(name = "OrderReference", namespace = XMLConstants.CAC)
    private OrderReference orderReference;

    @XmlElement(name = "DespatchDocumentReference", namespace = XMLConstants.CAC)
    private List<DespatchDocumentReference> despatchDocumentReferences;

    @XmlElement(name = "AdditionalDocumentReference", namespace = XMLConstants.CAC)
    private List<AdditionalDocumentReference> additionalDocumentReferences;

    @XmlElement(name = "Signature", namespace = XMLConstants.CAC)
    private XMLSignature signature;

    @XmlElement(name = "AccountingSupplierParty", namespace = XMLConstants.CAC)
    private AccountingSupplierParty accountingSupplierParty;

    @XmlElement(name = "AccountingCustomerParty", namespace = XMLConstants.CAC)
    private AccountingCustomerParty accountingCustomerParty;

    @XmlElement(name = "Delivery", namespace = XMLConstants.CAC)
    private Delivery delivery;

    @XmlElement(name = "PaymentMeans", namespace = XMLConstants.CAC)
    private PaymentMeans paymentMeans;

    @XmlElement(name = "PaymentTerms", namespace = XMLConstants.CAC)
    private List<PaymentTerms> paymentTerms;

    @XmlElement(name = "PrepaidPayment", namespace = XMLConstants.CAC)
    private List<PrepaidPayment> prepaidPayments;

    @XmlElement(name = "AllowanceCharge", namespace = XMLConstants.CAC)
    private List<AllowanceCharge> allowanceCharges;

    @XmlElement(name = "TaxTotal", namespace = XMLConstants.CAC)
    private TaxTotal taxTotal;

    // Note
    @XmlElement(name = "DiscrepancyResponse", namespace = XMLConstants.CAC)
    private DiscrepancyResponse discrepancyResponse;

    //

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.InvoiceTypeCode")
    @Data
    @NoArgsConstructor
    public static class InvoiceTypeCode {
        @XmlValue
        private String value;

        @XmlAttribute(name = "listID")
        private String listID;
    }


    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.Note")
    @Data
    @NoArgsConstructor
    public static class Note {
        @XmlValue
        private String value;

        @XmlAttribute(name = "languageLocaleID")
        private String languageLocaleId;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.OrderReference")
    @Data
    @NoArgsConstructor
    public static class OrderReference {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.DespatchDocumentReference")
    @Data
    @NoArgsConstructor
    public static class DespatchDocumentReference {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "DocumentTypeCode", namespace = XMLConstants.CBC)
        private String documentTypeCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.AdditionalDocumentReference")
    @Data
    @NoArgsConstructor
    public static class AdditionalDocumentReference {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "DocumentTypeCode", namespace = XMLConstants.CBC)
        private String documentTypeCode;

        @XmlElement(name = "DocumentStatusCode", namespace = XMLConstants.CBC)
        private String documentStatusCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.AccountingSupplierParty")
    @Data
    @NoArgsConstructor
    public static class AccountingSupplierParty {
        @XmlElement(name = "Party", namespace = XMLConstants.CAC)
        private XMLSupplier party;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.AccountingCustomerParty")
    @Data
    @NoArgsConstructor
    public static class AccountingCustomerParty {
        @XmlElement(name = "Party", namespace = XMLConstants.CAC)
        private XMLCustomer party;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.Delivery")
    @Data
    @NoArgsConstructor
    public static class Delivery {
        @XmlElement(name = "DeliveryLocation", namespace = XMLConstants.CAC)
        private DeliveryLocation deliveryLocation;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.DeliveryLocation")
    @Data
    @NoArgsConstructor
    public static class DeliveryLocation {
        @XmlElement(name = "Address", namespace = XMLConstants.CAC)
        private XMLAddress address;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.PaymentMeans")
    @Data
    @NoArgsConstructor
    public static class PaymentMeans {
        @XmlElement(name = "PaymentMeansCode", namespace = XMLConstants.CBC)
        private String paymentMeansCode;

        @XmlElement(name = "PayeeFinancialAccount", namespace = XMLConstants.CAC)
        private PayeeFinancialAccount payeeFinancialAccount;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.PayeeFinancialAccount")
    @Data
    @NoArgsConstructor
    public static class PayeeFinancialAccount {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.PaymentTerms")
    @Data
    @NoArgsConstructor
    public static class PaymentTerms {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "PaymentMeansID", namespace = XMLConstants.CBC)
        private String paymentMeansID;

        @XmlElement(name = "Amount", namespace = XMLConstants.CBC)
        private BigDecimal amount;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "PaymentDueDate", namespace = XMLConstants.CBC)
        private LocalDate paymentDueDate;

        @XmlElement(name = "PaymentPercent", namespace = XMLConstants.CBC)
        private BigDecimal paymentPercent;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.PrepaidPayment")
    @Data
    @NoArgsConstructor
    public static class PrepaidPayment {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "PaidAmount", namespace = XMLConstants.CBC)
        private BigDecimal paidAmount;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.AllowanceCharge")
    @Data
    @NoArgsConstructor
    public static class AllowanceCharge {
        @XmlElement(name = "ChargeIndicator", namespace = XMLConstants.CBC)
        private Boolean chargeIndicator;

        @XmlElement(name = "AllowanceChargeReasonCode", namespace = XMLConstants.CBC)
        private String allowanceChargeReasonCode;

        @XmlElement(name = "MultiplierFactorNumeric", namespace = XMLConstants.CBC)
        private BigDecimal multiplierFactorNumeric;

        @XmlElement(name = "Amount", namespace = XMLConstants.CBC)
        private BigDecimal amount;

        @XmlElement(name = "BaseAmount", namespace = XMLConstants.CBC)
        private BigDecimal baseAmount;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.TaxTotal")
    @Data
    @NoArgsConstructor
    public static class TaxTotal {
        @XmlElement(name = "TaxAmount", namespace = XMLConstants.CBC)
        BigDecimal taxAmount;

        @XmlElement(name = "TaxSubtotal", namespace = XMLConstants.CAC)
        List<TaxSubtotal> taxSubtotals;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.TaxSubtotal")
    @Data
    @NoArgsConstructor
    public static class TaxSubtotal {
        @XmlElement(name = "TaxableAmount", namespace = XMLConstants.CBC)
        private BigDecimal taxableAmount;

        @XmlElement(name = "TaxAmount", namespace = XMLConstants.CBC)
        private BigDecimal taxAmount;

        @XmlElement(name = "TaxCategory", namespace = XMLConstants.CAC)
        private TaxCategory taxCategory;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.TaxCategory")
    @Data
    @NoArgsConstructor
    public static class TaxCategory {
        @XmlElement(name = "TaxScheme", namespace = XMLConstants.CAC)
        private TaxScheme taxScheme;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.TaxScheme")
    @Data
    @NoArgsConstructor
    public static class TaxScheme {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.MonetaryTotal")
    @Data
    @NoArgsConstructor
    public static class MonetaryTotal {
        @XmlElement(name = "LineExtensionAmount", namespace = XMLConstants.CBC)
        private BigDecimal lineExtensionAmount;

        @XmlElement(name = "TaxInclusiveAmount", namespace = XMLConstants.CBC)
        private BigDecimal taxInclusiveAmount;

        @XmlElement(name = "AllowanceTotalAmount", namespace = XMLConstants.CBC)
        private BigDecimal allowanceTotalAmount;

        @XmlElement(name = "PrepaidAmount", namespace = XMLConstants.CBC)
        private BigDecimal prepaidAmount;

        @XmlElement(name = "PayableAmount", namespace = XMLConstants.CBC)
        private BigDecimal payableAmount;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SalesDocument.DiscrepancyResponse")
    @Data
    @NoArgsConstructor
    public static class DiscrepancyResponse {
        @XmlElement(name = "ReferenceID", namespace = XMLConstants.CBC)
        private String referenceID;

        @XmlElement(name = "ResponseCode", namespace = XMLConstants.CBC)
        private String responseCode;

        @XmlElement(name = "Description", namespace = XMLConstants.CBC)
        private String description;
    }
}
