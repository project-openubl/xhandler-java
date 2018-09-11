package io.github.carlosthe19916.webservices.utils;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UtilTest {

    @Test
    public void getErrorCode() {
        // Fault code: 100
        SOAPFault mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("100");

        SOAPFaultException exception = new SOAPFaultException(mockFault);
        Optional<Integer> result = Util.getErrorCode(exception);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // Fault code: soap-env:Client.100
        mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("soap-env:Client.100");

        exception = new SOAPFaultException(mockFault);
        result = Util.getErrorCode(exception);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // exception message: 100
        SOAPFaultException mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("100");

        result = Util.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());


        // exception message: soap-env:Client.100
        mockException = mock(SOAPFaultException.class);
        when(mockException.getMessage()).thenReturn("soap-env:Client.100");

        result = Util.getErrorCode(mockException);
        verify(mockFault).getFaultCode();

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(100, (int) result.get());
    }

    @Test
    public void getFileNameWithoutExtension() {
        Assert.assertEquals("file", Util.getFileNameWithoutExtension("file"));
        Assert.assertEquals("file", Util.getFileNameWithoutExtension("file."));
        Assert.assertEquals("file", Util.getFileNameWithoutExtension("file.xml"));
        Assert.assertEquals("file.md", Util.getFileNameWithoutExtension("file.md.xml"));
    }

}