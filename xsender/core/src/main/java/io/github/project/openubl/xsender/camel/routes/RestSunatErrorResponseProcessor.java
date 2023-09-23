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
import io.github.project.openubl.xsender.models.rest.ResponseDocumentErrorDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestSunatErrorResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpOperationFailedException httpException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
        ResponseDocumentErrorDto responseDocumentErrorDto = exchange.getIn().getBody(ResponseDocumentErrorDto.class);

        String responseCode = Optional.ofNullable(responseDocumentErrorDto.getCod())
                .orElse(responseDocumentErrorDto.getStatus());
        int responseCodeInt = Integer.parseInt(responseCode);

        String responseDescription = Optional.ofNullable(responseDocumentErrorDto.getMsg())
                .orElse(responseDocumentErrorDto.getMessage());

        List<String> notes = Optional.ofNullable(responseDocumentErrorDto.getErrors())
                .orElse(Collections.emptyList())
                .stream()
                .map(error -> error.getCod() + " - " + error.getMsg())
                .collect(Collectors.toList());

        SunatResponse sunatResponse = SunatResponse.builder()
                .status(Status.fromCode(responseCodeInt))
                .metadata(Metadata.builder()
                        .notes(notes)
                        .responseCode(responseCodeInt)
                        .description(responseDescription)
                        .build()
                )
                .build();

        exchange.getIn().setBody(sunatResponse);
    }

}
