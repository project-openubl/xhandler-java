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

import org.apache.camel.BindToRegistry;
import org.apache.camel.Configuration;
import org.apache.camel.PropertyInject;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.apache.cxf.ext.logging.LoggingFeature;
import service.sunat.gob.pe.billservice.BillService;
import service.sunat.gob.pe.billvalidservice.BillValidService;

@Configuration
public class CxfEndpointConfiguration {

    private CxfEndpoint getBasicCxfEndpoint(boolean enableLoggingFeature) {
        // Logging
        final LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);

        // Endpoint
        final CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setDefaultOperationNamespace("http://service.sunat.gob.pe");

        if (enableLoggingFeature) {
            cxfEndpoint.getFeatures().add(loggingFeature);
        }

        return cxfEndpoint;
    }

    @BindToRegistry("cxfBillServiceEndpoint")
    public CxfEndpoint cxfBillServiceEndpoint(@PropertyInject("enableLoggingFeature") boolean enableLoggingFeature) {
        final CxfEndpoint cxfEndpoint = getBasicCxfEndpoint(enableLoggingFeature);

        cxfEndpoint.setAddress("https://e-factura.sunat.gob.pe/ol-ti-itcpfegem/billService");
        cxfEndpoint.setServiceClass(BillService.class);
        cxfEndpoint.setServiceName("billService");
        cxfEndpoint.setWsdlURL("wsdl/billService.wsdl");

        return cxfEndpoint;
    }

    @BindToRegistry("cxfBillConsultServiceEndpoint")
    public CxfEndpoint cxfBillConsultServiceEndpoint(@PropertyInject("enableLoggingFeature") boolean enableLoggingFeature) {
        final CxfEndpoint cxfEndpoint = getBasicCxfEndpoint(enableLoggingFeature);

        cxfEndpoint.setAddress("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService");
        cxfEndpoint.setServiceClass(service.sunat.gob.pe.billconsultservice.BillService.class);
//        cxfEndpoint.setServiceName("billConsultService");
        cxfEndpoint.setWsdlURL("wsdl/billConsultService.wsdl");

        return cxfEndpoint;
    }

    @BindToRegistry("cxfBillValidServiceEndpoint")
    public CxfEndpoint cxfBillValidServiceEndpoint(@PropertyInject("enableLoggingFeature") boolean enableLoggingFeature) {
        final CxfEndpoint cxfEndpoint = getBasicCxfEndpoint(enableLoggingFeature);

        cxfEndpoint.setAddress("https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService");
        cxfEndpoint.setServiceClass(BillValidService.class);
//        cxfEndpoint.setServiceName("billValidService");
        cxfEndpoint.setWsdlURL("wsdl/billValidService.wsdl");

        return cxfEndpoint;
    }

}
