package io.github.carlosthe19916.webservices.managers.errorhandler;

import io.github.carlosthe19916.webservices.managers.errorhandler.billservice.BillService1033ErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.billservice.BillServiceRechazoErrorHandler;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BillServiceErrorHandlerManagerTest {

    @Test
    public void shouldBeSingleton() {
        BillServiceErrorHandlerManager instance1 = BillServiceErrorHandlerManager.getInstance();
        BillServiceErrorHandlerManager instance2 = BillServiceErrorHandlerManager.getInstance();

        Assert.assertEquals(instance1, instance2);
    }

    @Test
    public void shouldOrderHandlersByPriority() {
        BillServiceErrorHandlerManager instance = BillServiceErrorHandlerManager.getInstance();

        int rechazoIndex = -1;
        int error1033Index = -1;

        for (int i = 0; i < instance.errorHandlers.size(); i++) {
            BillServiceErrorHandler errorHandler = instance.errorHandlers.get(i);
            if (errorHandler instanceof BillServiceRechazoErrorHandler) {
                rechazoIndex = i;
            } else if (errorHandler instanceof BillService1033ErrorHandler) {
                error1033Index = i;
            }
        }

        Assert.assertTrue(error1033Index < rechazoIndex);
    }
}