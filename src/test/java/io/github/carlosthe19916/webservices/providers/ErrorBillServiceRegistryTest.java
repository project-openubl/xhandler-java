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