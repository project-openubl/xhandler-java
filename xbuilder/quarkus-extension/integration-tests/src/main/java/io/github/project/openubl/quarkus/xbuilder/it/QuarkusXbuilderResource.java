package io.github.project.openubl.quarkus.xbuilder.it;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.project.openubl.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.models.Defaults;
import io.github.project.openubl.xbuilder.models.Document;
import io.github.project.openubl.xhandler.core.Json;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/quarkus-xbuilder")
@Consumes(MediaType.APPLICATION_JSON)
public class QuarkusXbuilderResource {

    @Inject
    XBuilder xbuilder;

    /**
     * @param body {@code {"document": {"kind", "spec"}, "defaults": {...}}}
     */
    @POST
    @Path("build")
    @Produces(MediaType.APPLICATION_XML)
    public String build(String body) throws Exception {
        JsonNode input = Json.MAPPER.readTree(body);
        Defaults defaults = Json.MAPPER.treeToValue(input.get("defaults"), Defaults.class);
        return xbuilder.build(document(input.get("document")), defaults);
    }

    /**
     * Enriches with the defaults of the injected XBuilder (quarkus.xbuilder.*).
     *
     * @param body {@code {"kind", "spec"}}
     */
    @POST
    @Path("enrich")
    @Produces(MediaType.APPLICATION_JSON)
    public String enrich(String body) throws Exception {
        return Json.MAPPER.writeValueAsString(xbuilder.enrich(document(Json.MAPPER.readTree(body))));
    }

    private static Document document(JsonNode tagged) throws Exception {
        Class<?> model = Class.forName(Document.class.getPackageName() + "." + tagged.get("kind").asText());
        return (Document) Json.MAPPER.treeToValue(tagged.get("spec"), model);
    }
}
