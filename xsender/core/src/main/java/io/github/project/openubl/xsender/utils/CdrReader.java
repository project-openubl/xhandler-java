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
package io.github.project.openubl.xsender.utils;

import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class CdrReader {
    private final byte[] zipFile;
    private Status status;
    private final Metadata metadata;

    public CdrReader(byte[] zipFile) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        this.zipFile = zipFile;

        byte[] xml = ByteUtils.getFirstXmlFileFromZip(zipFile);
        Document document = ByteUtils.getDocumentFromBytes(xml);

        this.metadata = readContent(document);
        this.status = Status.fromCode(this.metadata.getResponseCode());
    }

    private Metadata readContent(Document document) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(
                new NamespaceContext() {
                    @Override
                    public String getNamespaceURI(String prefix) {
                        if ("ar".equals(prefix)) {
                            return "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2";
                        } else if ("ext".equals(prefix)) {
                            return "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2";
                        } else if ("cbc".equals(prefix)) {
                            return "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
                        } else if ("cac".equals(prefix)) {
                            return "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";
                        }
                        return null;
                    }

                    @Override
                    public String getPrefix(String s) {
                        return null;
                    }

                    @Override
                    public Iterator getPrefixes(String s) {
                        return null;
                    }
                }
        );

        XPathExpression codeXPathExpression = xPath.compile(
                "//ar:ApplicationResponse/cac:DocumentResponse/cac:Response/cbc:ResponseCode"
        );
        String code = (String) codeXPathExpression.evaluate(document, XPathConstants.STRING);

        XPathExpression descriptionXPathExpression = xPath.compile(
                "//ar:ApplicationResponse/cac:DocumentResponse/cac:Response/cbc:Description"
        );
        String description = (String) descriptionXPathExpression.evaluate(document, XPathConstants.STRING);

        XPathExpression notesXPathExpression = xPath.compile("//ar:ApplicationResponse/cbc:Note");
        NodeList noteNodes = (NodeList) notesXPathExpression.evaluate(document, XPathConstants.NODESET);
        List<String> notes = new ArrayList<>();
        for (int index = 0; index < noteNodes.getLength(); index++) {
            Node node = noteNodes.item(index);
            notes.add(node.getTextContent());
        }

        int responseCode = Integer.parseInt(code);
        return new Metadata(responseCode, description, notes);
    }
}
