package io.github.project.openubl.xsender.discovery;

public interface FileAnalyzer {

    ZipFile getZipFile();

    FileDeliveryTarget getFileDeliveryTarget();

    TicketDeliveryTarget getTicketDeliveryTarget();
}
