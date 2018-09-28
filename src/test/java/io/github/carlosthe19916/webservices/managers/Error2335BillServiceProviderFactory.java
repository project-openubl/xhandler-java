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

package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.providers.ErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.ErrorBillServiceProviderFactory;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class Error2335BillServiceProviderFactory implements ErrorBillServiceProviderFactory {

    static final int ERROR_CODE = 2_335;

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode) {
        return new Error2335BillServiceProvider(exceptionCode);
    }

    @Override
    public ErrorBillServiceProvider create(Integer exceptionCode, BillServiceModel previousResult) {
        return new Error2335BillServiceProvider(exceptionCode, previousResult);
    }

    @Override
    public boolean isSupported(int errorCode) {
        return errorCode == ERROR_CODE;
    }

    @Override
    public boolean isSupported(SOAPFaultException exception) {
        return Utils.getErrorCode(exception).orElse(-1) == ERROR_CODE;
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
