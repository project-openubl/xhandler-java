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
package io.github.project.openubl.xsender.models;

public enum Status {
    ACEPTADO,
    RECHAZADO,
    BAJA,
    EXCEPCION,
    EN_PROCESO,
    UNKNOWN;

    public static Status fromCode(int code) {
        if (code == 0) {
            return Status.ACEPTADO;
        } else if (code == 98) {
            return Status.EN_PROCESO;
        } else if (code == 99) {
            return Status.EXCEPCION;
        } else if (code >= 100 && code < 2_000) {
            return Status.EXCEPCION;
        } else if (code >= 2000 && code < 4000) {
            return Status.RECHAZADO;
        } else if (code >= 4000) {
            return Status.ACEPTADO;
        } else {
            return Status.UNKNOWN;
        }
    }
}
