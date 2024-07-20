package io.github.project.openubl.quarkus.xsender;

import io.github.project.openubl.quarkus.xsender.runtime.XSenderConfig;
import io.github.project.openubl.xsender.camel.routes.CxfEndpointConfiguration;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;

public class XSender {

    @Inject
    XSenderConfig config;

    @Produces
    @ApplicationScoped
    @Named("cxfBillServiceEndpoint")
    CxfEndpoint produceCxfBillServiceEndpoint() {
        return new CxfEndpointConfiguration().cxfBillServiceEndpoint(config.enableLoggingFeature);
    }

    @Produces
    @ApplicationScoped
    @Named("cxfBillConsultServiceEndpoint")
    CxfEndpoint produceCxfBillConsultServiceEndpoint() {
        return new CxfEndpointConfiguration().cxfBillConsultServiceEndpoint(config.enableLoggingFeature);
    }

    @Produces
    @ApplicationScoped
    @Named("cxfBillValidServiceEndpoint")
    CxfEndpoint produceCxfBillValidServiceEndpoint() {
        return new CxfEndpointConfiguration().cxfBillValidServiceEndpoint(config.enableLoggingFeature);
    }

}
