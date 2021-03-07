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

import io.github.project.openubl.xmlsenderws.webservices.exceptions.InvalidXMLFileException;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.UnsupportedDocumentTypeException;
import io.github.project.openubl.xmlsenderws.webservices.managers.smart.custom.CustomBillServiceConfig;
import io.github.project.openubl.xmlsenderws.webservices.managers.smart.custom.CustomSmartBillServiceManager;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.xml.XmlContentModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SmartBillServiceManager {

    private SmartBillServiceManager() {
        // Just static methods
    }

    public static SmartBillServiceModel send(File file, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        return send(file.toPath(), username, password);
    }

    public static SmartBillServiceModel send(Path path, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException, IOException {
        return send(Files.readAllBytes(path), username, password);
    }

    public static SmartBillServiceModel send(byte[] file, String username, String password) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
        SmartBillServiceConfig config = SmartBillServiceConfig.getInstance();

        return CustomSmartBillServiceManager.send(file, username, password, new CustomBillServiceConfig() {
            @Override
            public String getInvoiceAndNoteDeliveryURL() {
                return config.getInvoiceAndNoteDeliveryURL();
            }

            @Override
            public String getPerceptionAndRetentionDeliveryURL() {
                return config.getPerceptionAndRetentionDeliveryURL();
            }

            @Override
            public String getDespatchAdviceDeliveryURL() {
                return config.getDespatchAdviceDeliveryURL();
            }
        });
    }

    public static BillServiceModel getStatus(String ticket, XmlContentModel xmlContentModel, String username, String password) {
        SmartBillServiceConfig config = SmartBillServiceConfig.getInstance();

        return CustomSmartBillServiceManager.getStatus(ticket, xmlContentModel, username, password, new CustomBillServiceConfig() {
            @Override
            public String getInvoiceAndNoteDeliveryURL() {
                return config.getInvoiceAndNoteDeliveryURL();
            }

            @Override
            public String getPerceptionAndRetentionDeliveryURL() {
                return config.getPerceptionAndRetentionDeliveryURL();
            }

            @Override
            public String getDespatchAdviceDeliveryURL() {
                return config.getDespatchAdviceDeliveryURL();
            }
        });
    }

}
