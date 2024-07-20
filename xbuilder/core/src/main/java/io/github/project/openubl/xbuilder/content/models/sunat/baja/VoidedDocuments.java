package io.github.project.openubl.xbuilder.content.models.sunat.baja;

import io.github.project.openubl.xbuilder.content.models.sunat.SunatDocument;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoidedDocuments extends SunatDocument {

    /**
     * Lista de comprobantes a dar de baja
     */
    @Singular
    @ArraySchema(minItems = 1, schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    private List<VoidedDocumentsItem> comprobantes;
}
