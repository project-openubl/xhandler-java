package io.github.project.openubl.quickstart.xbuilder.jee;

import io.github.project.openubl.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.models.Invoice;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@ApplicationScoped
@Path("/")
public class UBLResource {

    private final XBuilder xbuilder = new XBuilder();

    @Inject
    UBLService ublService;

    @POST
    @Path("/create-xml")
    @Produces("text/plain")
    public String createInvoice(String client) {
        Invoice invoice = ublService.createInvoice(client);
        return xbuilder.build(invoice);
    }
}
