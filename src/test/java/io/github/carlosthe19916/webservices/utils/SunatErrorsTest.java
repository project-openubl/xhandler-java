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