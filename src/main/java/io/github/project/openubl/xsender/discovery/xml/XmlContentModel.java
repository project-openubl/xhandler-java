package io.github.project.openubl.xsender.discovery.xml;

public class XmlContentModel {

    private String documentType;
    private String documentID;
    private String ruc;
    private String voidedLineDocumentTypeCode;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getVoidedLineDocumentTypeCode() {
        return voidedLineDocumentTypeCode;
    }

    public void setVoidedLineDocumentTypeCode(String voidedLineDocumentTypeCode) {
        this.voidedLineDocumentTypeCode = voidedLineDocumentTypeCode;
    }

    public static final class Builder {
        private String documentType;
        private String documentID;
        private String ruc;
        private String voidedLineDocumentTypeCode;

        private Builder() {
        }

        public static Builder aXmlContentModel() {
            return new Builder();
        }

        public Builder withDocumentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder withDocumentID(String documentID) {
            this.documentID = documentID;
            return this;
        }

        public Builder withRuc(String ruc) {
            this.ruc = ruc;
            return this;
        }

        public Builder withVoidedLineDocumentTypeCode(String voidedLineDocumentTypeCode) {
            this.voidedLineDocumentTypeCode = voidedLineDocumentTypeCode;
            return this;
        }

        public XmlContentModel build() {
            XmlContentModel xmlContentModel = new XmlContentModel();
            xmlContentModel.setDocumentType(documentType);
            xmlContentModel.setDocumentID(documentID);
            xmlContentModel.setRuc(ruc);
            xmlContentModel.setVoidedLineDocumentTypeCode(voidedLineDocumentTypeCode);
            return xmlContentModel;
        }
    }
}
