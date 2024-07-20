package io.github.project.openubl.xsender.files;

import io.github.project.openubl.xsender.files.xml.XmlContent;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;

public interface BillServiceFileAnalyzer {
    ZipFile getZipFile();

    BillServiceDestination getSendFileDestination();

    BillServiceDestination getVerifyTicketDestination();

    XmlContent getXmlContent();
}
