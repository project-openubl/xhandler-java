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
package e2e.homologacion;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;

public class HomologacionConstants {

    public static final Proveedor proveedor = Proveedor.builder()
            .ruc("12345678912")
            .razonSocial("Softgreen S.A.C.")
            .build();
    public static final Cliente cliente = Cliente.builder()
            .nombre("Carlos Feria")
            .numeroDocumentoIdentidad("12121212121")
            .tipoDocumentoIdentidad(Catalog6.RUC.toString())
            .build();
}
