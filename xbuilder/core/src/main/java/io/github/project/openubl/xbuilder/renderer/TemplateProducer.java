package io.github.project.openubl.xbuilder.renderer;

import io.quarkus.qute.Template;

public class TemplateProducer {

    public Template getInvoice() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/invoice.xml");
    }

    public Template getCreditNote() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/creditNote.xml");
    }

    public Template getDebitNote() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/debitNote.xml");
    }

    public Template getVoidedDocument() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/voidedDocuments.xml");
    }

    public Template getSummaryDocuments() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/summaryDocuments.xml");
    }

    public Template getPerception() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/perception.xml");
    }

    public Template getRetention() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/retention.xml");
    }

    public Template getDespatchAdvice() {
        return EngineProducer.getInstance().getEngine().getTemplate("Renderer/despatchAdvice.xml");
    }

    private static class TemplateProducerHolder {

        private static final TemplateProducer INSTANCE = new TemplateProducer();
    }

    public static TemplateProducer getInstance() {
        return TemplateProducerHolder.INSTANCE;
    }
}
