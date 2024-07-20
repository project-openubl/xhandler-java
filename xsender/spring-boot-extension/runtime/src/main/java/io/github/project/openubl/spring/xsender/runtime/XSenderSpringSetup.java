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
