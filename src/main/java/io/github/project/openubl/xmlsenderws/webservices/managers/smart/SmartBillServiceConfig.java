/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
