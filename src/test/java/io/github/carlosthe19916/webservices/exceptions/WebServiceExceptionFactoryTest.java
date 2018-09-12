package io.github.carlosthe19916.webservices.exceptions;

import io.github.carlosthe19916.webservices.utils.Util;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebServiceExceptionFactoryTest {

    @Test
    public void createWebServiceException() {
        // Fault code: 100
        SOAPFault mockFault = mock(SOAPFault.class);
        when(mockFault.getFaultCode()).thenReturn("100");

        AbstractWebServiceException webServiceException = WebServiceExceptionFactory.createWebServiceException(new SOAPFaultException(mockFault));
        Assert.assertTrue(webServiceException instanceof ValidationWebServiceException);


        // Fault without code: for example when there is no internet
        mockFault = mock(SOAPFault.class);

        webServiceException = WebServiceExceptionFactory.createWebServiceException(new SOAPFaultException(mockFault));
        Assert.assertTrue(webServiceException instanceof UnknownWebServiceException);

    }
}