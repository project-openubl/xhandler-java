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

package io.github.carlosthe19916.webservices.utils;

import org.junit.Assert;
import org.junit.Test;

public class SunatErrorsTest {

    @Test
    public void shouldGetSingletonInstance() {
        SunatErrors instance1 = SunatErrors.getInstance();
        SunatErrors instance2 = SunatErrors.getInstance();

        Assert.assertEquals(instance1, instance2);
    }

    @Test
    public void shouldGetElement() {
        SunatErrors instance = SunatErrors.getInstance();
        Assert.assertEquals("El sistema no puede responder su solicitud. Intente nuevamente o comuníquese con su Administrador", instance.get(100));

        Assert.assertEquals("El sistema no puede responder su solicitud. Intente nuevamente o comuníquese con su Administrador", instance.getWithMaxLength(100, 10_000));
        Assert.assertEquals("El sistema", instance.getWithMaxLength(100, 10));
    }

}