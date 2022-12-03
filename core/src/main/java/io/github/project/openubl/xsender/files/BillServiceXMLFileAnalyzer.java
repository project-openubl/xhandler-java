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
package io.github.project.openubl.xsender.files;

import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.exceptions.UnsupportedXMLFileException;
import io.github.project.openubl.xsender.files.xml.DocumentType;
import io.github.project.openubl.xsender.files.xml.XmlContent;
import io.github.project.openubl.xsender.files.xml.XmlContentProvider;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import io.github.project.openubl.xsender.sunat.catalog.Catalog1;
import jodd.io.ZipBuilder;
import org.apache.cxf.helpers.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Pattern;

public class BillServiceXMLFileAnalyzer implements BillServiceFileAnalyzer {

    private static final String FILENAME_FORMAT1 = "{0}-{1}-{2}";
    private static final String FILENAME_FORMAT2 = "{0}-{1}";

    private final ZipFile zipFile;
    private final BillServiceDestination fileDestination;
    private final BillServiceDestination ticketDestination;

    private final XmlContent xmlContent;

    public BillServiceXMLFileAnalyzer(File file, CompanyURLs urLs) throws IOException, ParserConfigurationException, UnsupportedXMLFileException, SAXException {
        this(file.toPath(), urLs);
    }

    public BillServiceXMLFileAnalyzer(Path path, CompanyURLs urLs) throws IOException, ParserConfigurationException, UnsupportedXMLFileException, SAXException {
        this(Files.readAllBytes(path), urLs);
    }

    public BillServiceXMLFileAnalyzer(InputStream is, CompanyURLs urLs) throws IOException, ParserConfigurationException, UnsupportedXMLFileException, SAXException {
        this(IOUtils.readBytesFromStream(is), urLs);
    }

    public BillServiceXMLFileAnalyzer(byte[] file, CompanyURLs urls) throws ParserConfigurationException, IOException, SAXException, UnsupportedXMLFileException {
        this.xmlContent = XmlContentProvider.getSunatDocument(new ByteArrayInputStream(file));

        if (xmlContent.getDocumentType().equals(DocumentType.VOIDED_DOCUMENT)) {
            String voidedLineDocumentTypeCode = xmlContent.getVoidedLineDocumentTypeCode();
            Optional<Catalog1> catalog1Optional = Catalog1.valueOfCode(voidedLineDocumentTypeCode);
            if (catalog1Optional.isPresent() && catalog1Optional.get().equals(Catalog1.BOLETA)) {
            }
        }

        String fileNameWithoutExtension = BillServiceXMLFileAnalyzer
                .getFileNameWithoutExtension(xmlContent)
                .orElseThrow(() -> new UnsupportedXMLFileException("Couldn't infer the file name"));
        BillServiceDestination fileDestination = BillServiceXMLFileAnalyzer
                .getFileDeliveryTarget(urls, xmlContent)
                .orElseThrow(() -> new UnsupportedXMLFileException("Couldn't infer the delivery data"));
        BillServiceDestination ticketDestination = BillServiceXMLFileAnalyzer
                .getTicketDeliveryTarget(urls, xmlContent)
                .orElse(null);

        String zipFileName = fileNameWithoutExtension + ".zip";
        byte[] zipFile = ZipBuilder
                .createZipInMemory()
                .add(file)
                .path(fileNameWithoutExtension + ".xml")
                .save()
                .toBytes();

        this.zipFile = new ZipFile(zipFile, zipFileName);
        this.fileDestination = fileDestination;
        this.ticketDestination = ticketDestination;
    }

    @Override
    public ZipFile getZipFile() {
        return zipFile;
    }

    @Override
    public BillServiceDestination getSendFileDestination() {
        return fileDestination;
    }

    @Override
    public BillServiceDestination getVerifyTicketDestination() {
        return ticketDestination;
    }

    @Override
    public XmlContent getXmlContent() {
        return xmlContent;
    }

    private static Optional<String> getFileNameWithoutExtension(XmlContent xmlContent) {
        String documentType = xmlContent.getDocumentType();
        String documentID = xmlContent.getDocumentID();
        String ruc = xmlContent.getRuc();

        String result = null;
        String codigoDocumento;
        switch (documentType) {
            case DocumentType.INVOICE:
                if (Pattern.compile("^[F|f].*$").matcher(documentID).find()) {
                    codigoDocumento = Catalog1.FACTURA.getCode();
                } else if (Pattern.compile("^[B|b].*$").matcher(documentID).find()) {
                    codigoDocumento = Catalog1.BOLETA.getCode();
                } else {
                    throw new IllegalStateException("Invalid Serie, can not detect code");
                }

                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case DocumentType.CREDIT_NOTE:
                codigoDocumento = Catalog1.NOTA_CREDITO.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case DocumentType.DEBIT_NOTE:
                codigoDocumento = Catalog1.NOTA_DEBITO.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case DocumentType.VOIDED_DOCUMENT:
            case DocumentType.SUMMARY_DOCUMENT:
                result = MessageFormat.format(FILENAME_FORMAT2, ruc, documentID);
                break;
            case DocumentType.PERCEPTION:
                codigoDocumento = Catalog1.PERCEPCION.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case DocumentType.RETENTION:
                codigoDocumento = Catalog1.RETENCION.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
            case DocumentType.DESPATCH_ADVICE:
                codigoDocumento = Catalog1.GUIA_REMISION_REMITENTE.getCode();
                result = MessageFormat.format(FILENAME_FORMAT1, ruc, codigoDocumento, documentID);
                break;
        }

        return Optional.ofNullable(result);
    }

    public static Optional<BillServiceDestination> getFileDeliveryTarget(CompanyURLs urls, XmlContent xmlContent) {
        BillServiceDestination fileDeliveryTarget = null;

        switch (xmlContent.getDocumentType()) {
            case DocumentType.INVOICE:
            case DocumentType.CREDIT_NOTE:
            case DocumentType.DEBIT_NOTE:
                fileDeliveryTarget = new BillServiceDestination(urls.getInvoice(), BillServiceDestination.Operation.SEND_BILL);
                break;
            case DocumentType.SUMMARY_DOCUMENT:
                fileDeliveryTarget = new BillServiceDestination(urls.getInvoice(), BillServiceDestination.Operation.SEND_SUMMARY);
                break;
            case DocumentType.VOIDED_DOCUMENT:
                String tipoDocumentoAfectado = xmlContent.getVoidedLineDocumentTypeCode();
                Optional<Catalog1> catalog1Optional = Catalog1.valueOfCode(tipoDocumentoAfectado);
                if (!catalog1Optional.isPresent()) {
                    return Optional.empty();
                }

                Catalog1 catalog1 = catalog1Optional.get();
                String deliveryUrl;
                if (catalog1.equals(Catalog1.PERCEPCION) || catalog1.equals(Catalog1.RETENCION)) {
                    deliveryUrl = urls.getPerceptionRetention();
                } else if (catalog1.equals(Catalog1.GUIA_REMISION_REMITENTE)) {
                    deliveryUrl = urls.getDespatch();
                } else {
                    deliveryUrl = urls.getInvoice();
                }

                fileDeliveryTarget = new BillServiceDestination(deliveryUrl, BillServiceDestination.Operation.SEND_SUMMARY);
                break;
            case DocumentType.PERCEPTION:
            case DocumentType.RETENTION:
                fileDeliveryTarget =
                        new BillServiceDestination(urls.getPerceptionRetention(), BillServiceDestination.Operation.SEND_BILL);
                break;
            case DocumentType.DESPATCH_ADVICE:
                fileDeliveryTarget = new BillServiceDestination(urls.getDespatch(), BillServiceDestination.Operation.SEND_BILL);
                break;
        }

        return Optional.ofNullable(fileDeliveryTarget);
    }

    public static Optional<BillServiceDestination> getTicketDeliveryTarget(
            CompanyURLs urls,
            XmlContent xmlContent
    ) {
        boolean shouldVerifyTicket = false;
        switch (xmlContent.getDocumentType()) {
            case DocumentType.VOIDED_DOCUMENT:
            case DocumentType.SUMMARY_DOCUMENT:
                shouldVerifyTicket = true;
                break;
        }

        if (!shouldVerifyTicket) {
            return Optional.empty();
        }

        BillServiceDestination ticketDeliveryTarget;

        Catalog1 catalog1 = Catalog1
                .valueOfCode(xmlContent.getVoidedLineDocumentTypeCode())
                .orElse(Catalog1.FACTURA);
        switch (catalog1) {
            case PERCEPCION:
            case RETENCION:
                ticketDeliveryTarget =
                        new BillServiceDestination(urls.getPerceptionRetention(), BillServiceDestination.Operation.GET_STATUS);
                break;
            //            // No se pueden dar bajas de guias de remision
            //            case GUIA_REMISION_REMITENTE:
            //                ticketDeliveryTarget = new TicketDeliveryTarget(urls.getDespatch());
            //                break;
            default:
                ticketDeliveryTarget =
                        new BillServiceDestination(urls.getInvoice(), BillServiceDestination.Operation.GET_STATUS);
        }

        return Optional.of(ticketDeliveryTarget);
    }
}
