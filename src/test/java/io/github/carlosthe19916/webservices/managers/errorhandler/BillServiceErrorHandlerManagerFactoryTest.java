package io.github.carlosthe19916.webservices.managers.errorhandler;

import org.junit.Assert;
import org.junit.Test;

public class BillServiceErrorHandlerManagerFactoryTest {

    @Test
    public void shouldBeSingleton() {
        BillServiceErrorHandlerFactoryManager instance1 = BillServiceErrorHandlerFactoryManager.getInstance();
        BillServiceErrorHandlerFactoryManager instance2 = BillServiceErrorHandlerFactoryManager.getInstance();

        Assert.assertEquals(instance1, instance2);
    }

    @Test
    public void shouldOrderHandlersByPriority() {
        BillServiceErrorHandlerFactoryManager instance = BillServiceErrorHandlerFactoryManager.getInstance();

        int rechazoIndex = -1;
        int error1033Index = -1;

        for (int i = 0; i < instance.factories.size(); i++) {
            BillServiceErrorHandlerFactory factories = instance.factories.get(i);
            if (factories instanceof BillServiceRechazoErrorHandlerFactory) {
                rechazoIndex = i;
            } else if (factories instanceof BillService1033ErrorHandlerFactory) {
                error1033Index = i;
            }
        }

        Assert.assertTrue(error1033Index < rechazoIndex);
    }
}