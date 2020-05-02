package io.github.project.openubl.xmlsenderws.webservices.managers.smart;

public class SmartBillServiceConfig {

    private static volatile SmartBillServiceConfig instance;

    private String invoiceAndNoteDeliveryURL;
    private String perceptionAndRetentionDeliveryURL;
    private String despatchAdviceDeliveryURL;

    private SmartBillServiceConfig() {
        // Singleton
    }

    public static SmartBillServiceConfig getInstance() {
        if (instance == null) {
            synchronized (SmartBillServiceConfig.class) {
                if (instance == null) {
                    instance = new SmartBillServiceConfig();
                }
            }
        }
        return instance;
    }

    public String getInvoiceAndNoteDeliveryURL() {
        return invoiceAndNoteDeliveryURL;
    }

    public SmartBillServiceConfig withInvoiceAndNoteDeliveryURL(String invoiceAndNoteDeliveryURL) {
        this.invoiceAndNoteDeliveryURL = invoiceAndNoteDeliveryURL;
        return this;
    }

    public String getPerceptionAndRetentionDeliveryURL() {
        return perceptionAndRetentionDeliveryURL;
    }

    public SmartBillServiceConfig withPerceptionAndRetentionDeliveryURL(String perceptionAndRetentionDeliveryURL) {
        this.perceptionAndRetentionDeliveryURL = perceptionAndRetentionDeliveryURL;
        return this;
    }

    public String getDespatchAdviceDeliveryURL() {
        return despatchAdviceDeliveryURL;
    }

    public SmartBillServiceConfig withDespatchAdviceDeliveryURL(String despatchAdviceDeliveryURL) {
        this.despatchAdviceDeliveryURL = despatchAdviceDeliveryURL;
        return this;
    }
}
