package e2e;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import e2e.renderer.XMLAssertUtils;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.CreditNoteMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.DebitNoteMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.DespatchAdviceMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.InvoiceMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.PerceptionMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.RetentionMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.SummaryDocumentsMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.VoidedDocumentsMapper;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLCreditNote;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDebitNote;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDespatchAdvice;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLVoidedDocuments;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoice;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcion;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLRetention;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocuments;
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocuments;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Perception;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Retention;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocuments;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import io.github.project.openubl.xbuilder.enricher.config.DateProvider;
import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.renderer.TemplateProducer;
import io.quarkus.qute.Template;
import org.mapstruct.factory.Mappers;
import org.xml.sax.InputSource;

import jakarta.xml.bind.JAXBContext;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;

public class AbstractTest {

    private static final InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class);
    private static final CreditNoteMapper creditNoteMapper = Mappers.getMapper(CreditNoteMapper.class);
    private static final DebitNoteMapper debitNoteMapper = Mappers.getMapper(DebitNoteMapper.class);
    private static final VoidedDocumentsMapper voidedDocumentsMapper = Mappers.getMapper(VoidedDocumentsMapper.class);
    private static final SummaryDocumentsMapper summaryDocumentsMapper = Mappers.getMapper(SummaryDocumentsMapper.class);
    private static final PerceptionMapper perceptionMapper = Mappers.getMapper(PerceptionMapper.class);
    private static final RetentionMapper retentionMapper = Mappers.getMapper(RetentionMapper.class);
    private static final DespatchAdviceMapper despatchAdviceMapper = Mappers.getMapper(DespatchAdviceMapper.class);

    protected static final Defaults defaults = Defaults.builder()
            .icbTasa(new BigDecimal("0.2"))
            .igvTasa(new BigDecimal("0.18"))
            .ivapTasa(new BigDecimal("0.04"))
            .build();

    protected static final DateProvider dateProvider = () -> LocalDate.of(2019, 12, 24);

    public YAMLMapper getYamlMapper() {
        YAMLMapper mapper = new YAMLMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public void writeYaml(String kind, Object input, String snapshotFilename) throws URISyntaxException, IOException {
        String rootDir = getClass().getName().replaceAll("\\.", "/");

        String snapshotFileContent = Files.readString(Paths.get(getClass().getClassLoader().getResource(rootDir + "/" + snapshotFilename).toURI()));

        Path directoryPath = Paths.get("../quarkus-extension/integration-tests/src/test/resources").resolve(rootDir);
        Files.createDirectories(directoryPath);
        Path filePath = directoryPath.resolve(snapshotFilename.replaceAll(".xml", "") + ".yaml");

        getYamlMapper().writeValue(filePath.toFile(), Map.of(
                "kind", kind,
                "input", input,
                "snapshot", snapshotFileContent
        ));
    }

    protected void assertInput(Invoice input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLInvoice xmlPojo = (XMLInvoice) JAXBContext.newInstance(XMLInvoice.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            Invoice inputFromXml = invoiceMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.INVOICE_XSD);

        writeYaml("Invoice", input, snapshotFilename);
    }

    protected void assertInput(CreditNote input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getCreditNote();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLCreditNote xmlPojo = (XMLCreditNote) JAXBContext.newInstance(XMLCreditNote.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            CreditNote inputFromXml = creditNoteMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.CREDIT_NOTE_XSD);

        writeYaml("CreditNote", input, snapshotFilename);
    }

    protected void assertInput(DebitNote input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getDebitNote();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLDebitNote xmlPojo = (XMLDebitNote) JAXBContext.newInstance(XMLDebitNote.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            DebitNote inputFromXml = debitNoteMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.DEBIT_NOTE_XSD);

        writeYaml("DebitNote", input, snapshotFilename);
    }

    protected void assertInput(VoidedDocuments input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getVoidedDocument();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLVoidedDocuments xmlPojo = (XMLVoidedDocuments) JAXBContext.newInstance(XMLVoidedDocuments.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            VoidedDocuments inputFromXml = voidedDocumentsMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.VOIDED_DOCUMENTS_XSD);

        writeYaml("VoidedDocuments", input, snapshotFilename);
    }

    protected void assertInput(SummaryDocuments input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getSummaryDocuments();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLSummaryDocuments xmlPojo = (XMLSummaryDocuments) JAXBContext.newInstance(XMLSummaryDocuments.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            SummaryDocuments inputFromXml = summaryDocumentsMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.SUMMARY_DOCUMENTS_XSD);

        writeYaml("SummaryDocuments", input, snapshotFilename);
    }

    protected void assertInput(Perception input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getPerception();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLPercepcion xmlPojo = (XMLPercepcion) JAXBContext.newInstance(XMLPercepcion.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            Perception inputFromXml = perceptionMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.PERCEPTION_XSD);

        writeYaml("Perception", input, snapshotFilename);
    }

    protected void assertInput(Retention input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getRetention();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLRetention xmlPojo = (XMLRetention) JAXBContext.newInstance(XMLRetention.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            Retention inputFromXml = retentionMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.RETENTION_XSD);

        writeYaml("Retention", input, snapshotFilename);
    }

    protected void assertInput(DespatchAdvice input, String snapshotFilename) throws Exception {
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getDespatchAdvice();
        String xml = template.data(input).render();

        String reconstructedXml;
        try (StringReader reader = new StringReader(xml);) {
            XMLDespatchAdvice xmlPojo = (XMLDespatchAdvice) JAXBContext.newInstance(XMLDespatchAdvice.class)
                    .createUnmarshaller()
                    .unmarshal(new InputSource(reader));
            DespatchAdvice inputFromXml = despatchAdviceMapper.map(xmlPojo);
            reconstructedXml = template.data(inputFromXml).render();
        }

        // Then
        XMLAssertUtils.assertSnapshot(xml, reconstructedXml, getClass(), snapshotFilename);
        XMLAssertUtils.assertSendSunat(xml, XMLAssertUtils.DESPATCH_ADVICE_XSD);

        writeYaml("DespatchAdvice", input, snapshotFilename);
    }
}
