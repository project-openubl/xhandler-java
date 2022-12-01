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
package io.github.project.openubl.quarkus.xsender;

import io.github.project.openubl.quarkus.xsender.runtime.XSenderConfig;
import io.github.project.openubl.xsender.camel.routes.CxfEndpointConfiguration;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

public class XSender {

    @Inject
    XSenderConfig config;

    @Produces
    @ApplicationScoped
    @Named("cxfEndpoint")
    CxfEndpoint produceCxfEndpoint() {
        return new CxfEndpointConfiguration().cxfEndpoint(config.enableLoggingFeature);
    }

}
