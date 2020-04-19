/*
 * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.projectopenubl.xmlsenderlib.webservices.models;

import java.util.Optional;
import java.util.stream.Stream;

public enum GetStatusCdrResponseType {

    CONSTANCIA_EXISTE("0004", "La constancia existe"),
    CONSTANCIA__NO_EXISTE("0127", "El ticket no existe");

    private final String code;
    private final String description;

    GetStatusCdrResponseType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<GetStatusCdrResponseType> searchByCode(String code) {
        return Stream.of(GetStatusCdrResponseType.values()).filter(p -> p.getCode().equals(code)).findFirst();
    }

}
