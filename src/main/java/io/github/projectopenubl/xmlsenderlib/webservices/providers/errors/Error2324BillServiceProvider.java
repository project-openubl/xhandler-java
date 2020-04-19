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

package io.github.projectopenubl.xmlsenderlib.webservices.providers.errors;

import io.github.projectopenubl.xmlsenderlib.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.projectopenubl.xmlsenderlib.webservices.providers.BillServiceModel;
import io.github.projectopenubl.xmlsenderlib.webservices.wrappers.ServiceConfig;

public class Error2324BillServiceProvider extends AbstractErrorBillServiceProvider {

    private final Integer exceptionCode;
    private final BillServiceModel previousResult;

    public Error2324BillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public Error2324BillServiceProvider(Integer exceptionCode, BillServiceModel previousResult) {
        this.exceptionCode = exceptionCode;
        this.previousResult = previousResult;
    }

    @Override
    public BillServiceModel getStatus(String ticket, ServiceConfig config) {
        if (previousResult != null && previousResult.getCdr() != null) {
            BillServiceModel result = new BillServiceModel();

            // Copy info
            result.setTicket(previousResult.getTicket());
            result.setDescription(previousResult.getDescription());
            result.setCode(previousResult.getCode());
            result.setCdr(previousResult.getCdr());

            // Change status
            result.setStatus(BillServiceModel.Status.ACEPTADO);
            return result;
        }
        return null;
    }
}
