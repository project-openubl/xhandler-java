/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xmlsenderws.webservices.exceptions;

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

        ValidationWebServiceException validationWebServiceException = (ValidationWebServiceException) webServiceException;
        Assert.assertEquals(Integer.valueOf(100), validationWebServiceException.getSUNATErrorCode());
        Assert.assertEquals("El sistema no puede responder su solicitud. Intente nuevamente o comuníquese con su Administrador", validationWebServiceException.getSUNATErrorMessage());
        Assert.assertEquals("El sistema", validationWebServiceException.getSUNATErrorMessage(10));


        // Fault without code: for example when there is no internet
        mockFault = mock(SOAPFault.class);

        webServiceException = WebServiceExceptionFactory.createWebServiceException(new SOAPFaultException(mockFault));
        Assert.assertTrue(webServiceException instanceof UnknownWebServiceException);
        Assert.assertNotNull(webServiceException.getException());
    }
}
