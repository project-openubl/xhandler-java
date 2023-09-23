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
package io.github.project.openubl.spring.xsender.runtime;

import io.github.project.openubl.xsender.camel.routes.CxfEndpointConfiguration;
import io.github.project.openubl.xsender.camel.routes.SunatRouteBuilder;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XSenderSpringSetup {

    @Value("${spring.xsender.enable-logging-feature:false}")
    private boolean enableLoggingFeature;

    @Bean
    public SunatRouteBuilder produceSunatRouteBuilder() {
        return new SunatRouteBuilder();
    }

    @Bean(name = "cxfBillServiceEndpoint")
    CxfEndpoint produceCxfBillServiceEndpoint() {
        return new CxfEndpointConfiguration().cxfBillServiceEndpoint(enableLoggingFeature);
    }

    @Bean(name = "cxfBillConsultServiceEndpoint")
    CxfEndpoint produceCxfBillConsultServiceEndpoint() {
        return new CxfEndpointConfiguration().cxfBillConsultServiceEndpoint(enableLoggingFeature);
    }

    @Bean(name = "cxfBillValidServiceEndpoint")
    CxfEndpoint produceCxfBillValidServiceEndpoint() {
        return new CxfEndpointConfiguration().cxfBillValidServiceEndpoint(enableLoggingFeature);
    }

}
