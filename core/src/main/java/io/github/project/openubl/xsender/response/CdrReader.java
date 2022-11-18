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
package io.github.project.openubl.xsender.response;

import io.github.project.openubl.xsender.utils.Utils;
import java.io.IOException;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CdrReader {

    private final byte[] cdrZip;

    private final CdrModel cdrModel;
    private final Optional<CdrStatus> cdrStatus;

    public CdrReader(byte[] cdrZip)
        throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        this.cdrZip = cdrZip;

        byte[] xml = Utils.getFirstXmlFileFromZip(cdrZip);
        Document document = Utils.getDocumentFromBytes(xml);
        this.cdrModel = DocumentToModel.toCdrModel(document);

        this.cdrStatus = CdrStatus.fromCode(this.cdrModel.getResponseCode());
    }

    public byte[] getCdrZip() {
        return cdrZip;
    }

    public CdrModel getCdrModel() {
        return cdrModel;
    }

    public Optional<CdrStatus> getCdrStatus() {
        return cdrStatus;
    }
}
