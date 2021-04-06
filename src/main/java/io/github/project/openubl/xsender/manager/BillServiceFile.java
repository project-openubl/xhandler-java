package io.github.project.openubl.xsender.manager;

public class BillServiceFile {

    private String filename;
    private byte[] file;

    public BillServiceFile(String filename, byte[] file) {
        this.filename = filename;
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
