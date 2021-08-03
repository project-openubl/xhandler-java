package io.github.project.openubl.xsender.discovery.xml;

public final class XmlContentModelBuilder {
    private String documentType;
    private String documentID;
    private String ruc;
    private String voidedLineDocumentTypeCode;

    private XmlContentModelBuilder() {
    }

    public static XmlContentModelBuilder aXmlContentModel() {
        return new XmlContentModelBuilder();
    }

    public XmlContentModelBuilder withDocumentType(String documentType) {
        this.documentType = documentType;
        return this;
    }

    public XmlContentModelBuilder withDocumentID(String documentID) {
        this.documentID = documentID;
        return this;
    }

    public XmlContentModelBuilder withRuc(String ruc) {
        this.ruc = ruc;
        return this;
    }

    public XmlContentModelBuilder withVoidedLineDocumentTypeCode(String voidedLineDocumentTypeCode) {
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
