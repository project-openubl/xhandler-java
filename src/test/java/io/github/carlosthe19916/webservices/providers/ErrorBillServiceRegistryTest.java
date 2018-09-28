/*
 *
 *  * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 *  * and other contributors as indicated by the @author tags.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.carlosthe19916.webservices.providers;

import io.github.carlosthe19916.webservices.providers.errors.Error1033BillServiceProviderFactory;
import io.github.carlosthe19916.webservices.providers.errors.ErrorRechazoBillServiceProviderFactory;
import org.junit.Assert;
import org.junit.Test;

public class ErrorBillServiceRegistryTest {

    @Test
    public void shouldBeSingleton() {
        ErrorBillServiceRegistry instance1 = ErrorBillServiceRegistry.getInstance();
        ErrorBillServiceRegistry instance2 = ErrorBillServiceRegistry.getInstance();

        Assert.assertEquals(instance1, instance2);
    }

    @Test
    public void shouldOrderHandlersByPriority() {
        ErrorBillServiceRegistry instance = ErrorBillServiceRegistry.getInstance();

        int rechazoIndex = -1;
        int error1033Index = -1;

        for (int i = 0; i < instance.factories.size(); i++) {
            ErrorBillServiceProviderFactory factories = instance.factories.get(i);
            if (factories instanceof ErrorRechazoBillServiceProviderFactory) {
                rechazoIndex = i;
            } else if (factories instanceof Error1033BillServiceProviderFactory) {
                error1033Index = i;
            }
        }

        Assert.assertTrue(error1033Index < rechazoIndex);
    }

}