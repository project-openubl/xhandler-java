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

import org.apache.camel.builder.RouteBuilder;
import org.apache.cxf.binding.soap.SoapFault;

public class CxfRouteBuilder extends RouteBuilder {

    public static final String XSENDER_URI = "direct:xsender";

    @Override
    public void configure() {
        from(XSENDER_URI)
                .id("xsender")
                .to("cxf://bean:cxfEndpoint?dataFormat=POJO")
                .process(new SunatResponseProcessor())
                .onException(SoapFault.class).handled(true).process(new SunatErrorResponseProcessor()).end().end();
    }
}
