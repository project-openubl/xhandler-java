package io.github.project.openubl.xbuilder.content.jaxb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import java.math.BigDecimal;
import java.util.List;

@XmlType(name = "SummaryDocumentsLine")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public class XMLSummaryDocumentsLine {

    @XmlElement(name = "DocumentTypeCode", namespace = XMLConstants.CBC)
    private String documentTypeCode;

    @XmlElement(name = "ID", namespace = XMLConstants.CBC)
    private String documentId;

    @XmlElement(name = "AccountingCustomerParty", namespace = XMLConstants.CAC)
    private AccountingCustomerParty accountingCustomerParty;

    @XmlElement(name = "BillingReference", namespace = XMLConstants.CAC)
    private BillingReference billingReference;

    @XmlElement(name = "Status", namespace = XMLConstants.CAC)
    private Status status;

    @XmlElement(name = "TotalAmount", namespace = XMLConstants.SAC)
    private TotalAmount totalAmount;

    @XmlElement(name = "BillingPayment", namespace = XMLConstants.SAC)
    private List<BillingPayment> billingPayments;

    @XmlElement(name = "AllowanceCharge", namespace = XMLConstants.CAC)
    private AllowanceCharge allowanceCharge;

    @XmlElement(name = "TaxTotal", namespace = XMLConstants.CAC)
    private List<TaxTotal> taxTotals;

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.AccountingCustomerParty")
    @Data
    @NoArgsConstructor
    public static class AccountingCustomerParty {
        @XmlElement(name = "CustomerAssignedAccountID", namespace = XMLConstants.CBC)
        private String customerAssignedAccountID;

        @XmlElement(name = "AdditionalAccountID", namespace = XMLConstants.CBC)
        private String additionalAccountID;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.Status")
    @Data
    @NoArgsConstructor
    public static class Status {
        @XmlElement(name = "ConditionCode", namespace = XMLConstants.CBC)
        private String conditionCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.TotalAmount")
    @Data
    @NoArgsConstructor
    public static class TotalAmount {
        @XmlValue
        private BigDecimal value;

        @XmlAttribute(name = "currencyID")
        private String currencyID;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.AllowanceCharge")
    @Data
    @NoArgsConstructor
    public static class AllowanceCharge {
        @XmlElement(name = "Amount", namespace = XMLConstants.CBC)
        private BigDecimal value;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.BillingPayment")
    @Data
    @NoArgsConstructor
    public static class BillingPayment {
        @XmlElement(name = "PaidAmount", namespace = XMLConstants.CBC)
        BigDecimal paidAmount;

        @XmlElement(name = "InstructionID", namespace = XMLConstants.CBC)
        String instructionId;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.BillingReference")
    @Data
    @NoArgsConstructor
    public static class BillingReference {
        @XmlElement(name = "InvoiceDocumentReference", namespace = XMLConstants.CAC)
        private InvoiceDocumentReference invoiceDocumentReference;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.InvoiceDocumentReference")
    @Data
    @NoArgsConstructor
    public static class InvoiceDocumentReference {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;

        @XmlElement(name = "DocumentTypeCode", namespace = XMLConstants.CBC)
        private String documentTypeCode;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.TaxTotalSummaryDocuments")
    @Data
    @NoArgsConstructor
    public static class TaxTotal {
        @XmlElement(name = "TaxAmount", namespace = XMLConstants.CBC)
        BigDecimal taxAmount;

        @XmlElement(name = "TaxSubtotal", namespace = XMLConstants.CAC)
        TaxSubtotal taxSubtotals;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.TaxSubtotalTaxTotalSummaryDocuments")
    @Data
    @NoArgsConstructor
    public static class TaxSubtotal {
        @XmlElement(name = "TaxAmount", namespace = XMLConstants.CBC)
        private BigDecimal taxAmount;

        @XmlElement(name = "TaxCategory", namespace = XMLConstants.CAC)
        private TaxCategory taxCategory;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.TaxCategory")
    @Data
    @NoArgsConstructor
    public static class TaxCategory {
        @XmlElement(name = "TaxScheme", namespace = XMLConstants.CAC)
        private TaxScheme taxScheme;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "SummaryDocumentsLine.TaxScheme")
    @Data
    @NoArgsConstructor
    public static class TaxScheme {
        @XmlElement(name = "ID", namespace = XMLConstants.CBC)
        private String id;
    }
}
