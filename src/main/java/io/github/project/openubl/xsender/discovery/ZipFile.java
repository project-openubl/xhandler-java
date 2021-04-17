package io.github.project.openubl.xsender.discovery;

public class ZipFile {

    private final byte[] file;
    private final String filename;

    public ZipFile(byte[] file, String filename) {
        this.file = file;
        this.filename = filename;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

}
