package io.github.project.openubl.quickstart.xbuilder.jee;

import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import io.github.project.openubl.xbuilder.enricher.config.DateProvider;
import io.github.project.openubl.xbuilder.enricher.config.Defaults;
import io.github.project.openubl.xbuilder.renderer.TemplateProducer;
import io.quarkus.qute.Template;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.math.BigDecimal;
import java.time.LocalDate;

@ApplicationScoped
@Path("/")
public class UBLResource {

    Defaults defaults = Defaults.builder()
            .icbTasa(new BigDecimal("0.2"))
            .igvTasa(new BigDecimal("0.18"))
            .build();

    DateProvider dateProvider = LocalDate::now;

    @Inject
    UBLService ublService;

    @POST
    @Path("/create-xml")
    @Produces("text/plain")
    public String createInvoice(String client) {
        // Invoice generation
        Invoice invoice = ublService.createInvoice(client);

        // Enrich data
        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(invoice);

        // Generate XML
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(invoice).render();

        return xml;
    }

}
