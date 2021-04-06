/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * <p>
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.eclipse.org/legal/epl-2.0/
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xsender.manager;

import io.github.project.openubl.xmlsenderws.webservices.models.InternetMediaType;
import jodd.io.ZipBuilder;
import service.sunat.gob.pe.billservice.BillService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;

public class BillServiceAdapter {

    private final BillService billService;

    public BillServiceAdapter(BillService billService) {
        this.billService = billService;
    }

    public byte[] sendBill(String fileName, byte[] file) {
        BillServiceFile zipFile = getZipFile(fileName, file);
        DataSource dataSource = new ByteArrayDataSource(zipFile.getFile(), InternetMediaType.ZIP.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendBill(zipFile.getFilename(), dataHandler, null);
    }

    public String sendSummary(String fileName, byte[] file) {
        return sendSummaryOrPack(fileName, file, true);
    }

    public String sendPack(String fileName, byte[] file) {
        return sendSummaryOrPack(fileName, file, false);
    }

    public String getStatus(String ticket) {
        return billService.getStatus(ticket);
    }

    private BillServiceFile getZipFile(String fileName, byte[] file) {
        String zipFileName = fileName;
        byte[] zipFile = file;

        if (fileName.endsWith(".xml")) {
            try {
                zipFile = ZipBuilder.createZipInMemory()
                        .add(file)
                        .path(fileName)
                        .save()
                        .toBytes();
                zipFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return new BillServiceFile(zipFileName, zipFile);
    }

    private String sendSummaryOrPack(String fileName, byte[] file, boolean isSummary) {
        BillServiceFile zipFile = getZipFile(fileName, file);

        DataSource dataSource = new ByteArrayDataSource(zipFile.getFile(), InternetMediaType.ZIP.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);

        String ticket;
        if (isSummary) {
            ticket = billService.sendSummary(zipFile.getFilename(), dataHandler, null);
        } else {
            ticket = billService.sendPack(zipFile.getFilename(), dataHandler, null);
        }
        return ticket;
    }

}
