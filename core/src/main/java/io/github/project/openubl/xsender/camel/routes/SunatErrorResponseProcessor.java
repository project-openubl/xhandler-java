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
package io.github.project.openubl.xsender.camel.routes;

import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePropertyKey;
import org.apache.camel.Processor;
import org.apache.cxf.binding.soap.SoapFault;

import javax.xml.namespace.QName;
import java.util.Collections;

public class SunatErrorResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        SoapFault fault = exchange.getProperty(ExchangePropertyKey.EXCEPTION_CAUGHT, SoapFault.class);
        String message = fault.getMessage();

        QName qName = fault.getFaultCode();
        String localPart = qName.getLocalPart();
        String errorCode = localPart.replaceAll("Client.", "");
        int errorCodeInt = Integer.parseInt(errorCode);

        Metadata metadata = Metadata.builder()
                .notes(Collections.emptyList())
                .responseCode(errorCodeInt)
                .description(message)
                .build();

        SunatResponse sunatResponse = SunatResponse.builder()
                .status(Status.fromCode(errorCodeInt))
                .metadata(metadata)
                .build();
        exchange.getIn().setBody(sunatResponse);
    }

}
