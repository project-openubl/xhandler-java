package io.github.carlosthe19916.webservices.managers;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SUNATCodigoErroresTest {

    @Test
    public void shouldGetSingletonInstance() {
        SUNATCodigoErrores instance1 = SUNATCodigoErrores.getInstance();
        SUNATCodigoErrores instance2 = SUNATCodigoErrores.getInstance();

        Assert.assertEquals(instance1, instance2);
    }

    @Test
    public void shouldGetElement() {
        SUNATCodigoErrores instance = SUNATCodigoErrores.getInstance();
        Assert.assertEquals("El sistema no puede responder su solicitud. Intente nuevamente o comuníquese con su Administrador", instance.get(100));

        Assert.assertEquals("El sistema no puede responder su solicitud. Intente nuevamente o comuníquese con su Administrador", instance.getWithMaxLength(100, 10_000));
        Assert.assertEquals("El sistema", instance.getWithMaxLength(100, 10));
    }

}