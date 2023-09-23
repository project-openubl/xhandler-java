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
package io.github.project.openubl.quarkus.xbuilder;

import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.quarkus.qute.Template;

public interface XBuilder {
    Template getTemplate(Type type);

    Defaults getDefaults();

    enum Type {
        INVOICE("invoice.xml"),
        CREDIT_NOTE("creditNote.xml"),
        DEBIT_NOTE("debitNote.xml"),
        VOIDED_DOCUMENTS("voidedDocuments.xml"),
        SUMMARY_DOCUMENTS("summaryDocuments.xml"),
        PERCEPTION("perception.xml"),
        RETENTION("retention.xml"),
        DESPATCH_ADVICE("despatchAdvice.xml");

        private final String templatePath;

        Type(String templatePath) {
            this.templatePath = templatePath;
        }

        public String getTemplatePath() {
            return templatePath;
        }
    }
}
