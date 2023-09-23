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
