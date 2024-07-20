package io.github.project.openubl.xbuilder.content.models.standard.general;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Clase base para CreditNote y DebitNote.
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Jacksonized
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditNote extends Note {
}
