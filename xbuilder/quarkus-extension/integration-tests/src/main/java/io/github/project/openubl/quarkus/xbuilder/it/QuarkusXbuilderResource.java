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
package io.github.project.openubl.quarkus.xbuilder.it;

import io.github.project.openubl.quarkus.xbuilder.XBuilder;
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
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLInvoice;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLPercepcion;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLRetention;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSummaryDocuments;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLVoidedDocuments;
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocuments;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Perception;
import io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion.Retention;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocuments;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import io.quarkus.qute.Template;
import io.vertx.core.json.JsonObject;
import org.mapstruct.factory.Mappers;
import org.xml.sax.InputSource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.CREDIT_NOTE;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.DEBIT_NOTE;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.DESPATCH_ADVICE;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.INVOICE;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.PERCEPTION;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.RETENTION;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.SUMMARY_DOCUMENTS;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.VOIDED_DOCUMENTS;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
@ApplicationScoped
@Path("/quarkus-xbuilder")
public class QuarkusXbuilderResource {

    @Inject
    XBuilder xBuilder;

    @Inject
    Unmarshaller unmarshaller;

    private static final InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class);
    private static final CreditNoteMapper creditNoteMapper = Mappers.getMapper(CreditNoteMapper.class);
    private static final DebitNoteMapper debitNoteMapper = Mappers.getMapper(DebitNoteMapper.class);
    private static final VoidedDocumentsMapper voidedDocumentsMapper = Mappers.getMapper(VoidedDocumentsMapper.class);
    private static final SummaryDocumentsMapper summaryDocumentsMapper = Mappers.getMapper(SummaryDocumentsMapper.class);
    private static final PerceptionMapper perceptionMapper = Mappers.getMapper(PerceptionMapper.class);
    private static final RetentionMapper retentionMapper = Mappers.getMapper(RetentionMapper.class);
    private static final DespatchAdviceMapper despatchAdviceMapper = Mappers.getMapper(DespatchAdviceMapper.class);

    @POST
    @Path("Invoice/from-json")
    public String createInvoiceXml(JsonObject json) {
        Invoice invoice = json.mapTo(Invoice.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(invoice);

        Template template = xBuilder.getTemplate(INVOICE);
        return template.data(invoice).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("Invoice/from-xml")
    public String createInvoiceXml(String xml) throws IOException, JAXBException {
        Template template = xBuilder.getTemplate(INVOICE);

        try (StringReader reader = new StringReader(xml)) {
            XMLInvoice xmlPojo = (XMLInvoice) unmarshaller.unmarshal(new InputSource(reader));
            Invoice inputFromXml = invoiceMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("CreditNote/from-json")
    public String createCreditNote(JsonObject json) {
        CreditNote creditNote = json.mapTo(CreditNote.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(creditNote);

        Template template = xBuilder.getTemplate(CREDIT_NOTE);
        return template.data(creditNote).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("CreditNote/from-xml")
    public String createCreditNoteXml(String xml) {
        Template template = xBuilder.getTemplate(CREDIT_NOTE);

        try (StringReader reader = new StringReader(xml)) {
            XMLCreditNote xmlPojo = (XMLCreditNote) unmarshaller.unmarshal(new InputSource(reader));
            CreditNote inputFromXml = creditNoteMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("DebitNote/from-json")
    public String createDebitNote(JsonObject json) {
        DebitNote debitNote = json.mapTo(DebitNote.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(debitNote);

        Template template = xBuilder.getTemplate(DEBIT_NOTE);
        return template.data(debitNote).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("DebitNote/from-xml")
    public String createDebitNoteXml(String xml) {
        Template template = xBuilder.getTemplate(DEBIT_NOTE);

        try (StringReader reader = new StringReader(xml)) {
            XMLDebitNote xmlPojo = (XMLDebitNote) unmarshaller.unmarshal(new InputSource(reader));
            DebitNote inputFromXml = debitNoteMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("VoidedDocuments/from-json")
    public String createVoidedDocuments(JsonObject json) {
        VoidedDocuments voidedDocuments = json.mapTo(VoidedDocuments.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(voidedDocuments);

        Template template = xBuilder.getTemplate(VOIDED_DOCUMENTS);
        return template.data(voidedDocuments).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("VoidedDocuments/from-xml")
    public String createVoidedDocumentsXml(String xml) {
        Template template = xBuilder.getTemplate(VOIDED_DOCUMENTS);

        try (StringReader reader = new StringReader(xml)) {
            XMLVoidedDocuments xmlPojo = (XMLVoidedDocuments) unmarshaller.unmarshal(new InputSource(reader));
            VoidedDocuments inputFromXml = voidedDocumentsMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("SummaryDocuments/from-json")
    public String createSummaryDocuments(JsonObject json) {
        SummaryDocuments summaryDocuments = json.mapTo(SummaryDocuments.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(summaryDocuments);

        Template template = xBuilder.getTemplate(SUMMARY_DOCUMENTS);
        return template.data(summaryDocuments).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("SummaryDocuments/from-xml")
    public String createSummaryDocumentsXml(String xml) {
        Template template = xBuilder.getTemplate(SUMMARY_DOCUMENTS);

        try (StringReader reader = new StringReader(xml)) {
            XMLSummaryDocuments xmlPojo = (XMLSummaryDocuments) unmarshaller.unmarshal(new InputSource(reader));
            SummaryDocuments inputFromXml = summaryDocumentsMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("Perception/from-json")
    public String createPerception(JsonObject json) {
        Perception perception = json.mapTo(Perception.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(perception);

        Template template = xBuilder.getTemplate(PERCEPTION);
        return template.data(perception).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("Perception/from-xml")
    public String createPerceptionXml(String xml) {
        Template template = xBuilder.getTemplate(PERCEPTION);

        try (StringReader reader = new StringReader(xml)) {
            XMLPercepcion xmlPojo = (XMLPercepcion) unmarshaller.unmarshal(new InputSource(reader));
            Perception inputFromXml = perceptionMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("Retention/from-json")
    public String createRetention(JsonObject json) {
        Retention retention = json.mapTo(Retention.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(retention);

        Template template = xBuilder.getTemplate(RETENTION);
        return template.data(retention).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("Retention/from-xml")
    public String createRetentionXml(String xml) {
        Template template = xBuilder.getTemplate(RETENTION);

        try (StringReader reader = new StringReader(xml)) {
            XMLRetention xmlPojo = (XMLRetention) unmarshaller.unmarshal(new InputSource(reader));
            Retention inputFromXml = retentionMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("DespatchAdvice/from-json")
    public String createDespatchAdvice(JsonObject json) {
        DespatchAdvice despatchAdvice = json.mapTo(DespatchAdvice.class);

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(despatchAdvice);

        Template template = xBuilder.getTemplate(DESPATCH_ADVICE);
        return template.data(despatchAdvice).render();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("DespatchAdvice/from-xml")
    public String createDespatchAdviceXml(String xml) {
        Template template = xBuilder.getTemplate(DESPATCH_ADVICE);

        try (StringReader reader = new StringReader(xml)) {
            XMLDespatchAdvice xmlPojo = (XMLDespatchAdvice) unmarshaller.unmarshal(new InputSource(reader));
            DespatchAdvice inputFromXml = despatchAdviceMapper.map(xmlPojo);
            return template.data(inputFromXml).render();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
