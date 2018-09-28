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

package io.github.carlosthe19916.webservices.exceptions;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import static org.mockito.Mockito.mock;
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