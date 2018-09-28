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

package io.github.carlosthe19916.webservices.providers.errors;

import io.github.carlosthe19916.webservices.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.utils.SunatErrors;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public class ErrorRechazoBillServiceProvider extends AbstractErrorBillServiceProvider {

    private final BillServiceModel previousResult;
    private final Integer exceptionCode;

    public ErrorRechazoBillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public ErrorRechazoBillServiceProvider(BillServiceModel previousResult, Integer exceptionCode) {
        this.previousResult = previousResult;
        this.exceptionCode = exceptionCode;
    }

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {
        BillServiceModel result = new BillServiceModel();

        if (previousResult == null) {
            String errorMessage = SunatErrors.getInstance().get(exceptionCode);

            result.setStatus(BillServiceModel.Status.RECHAZADO);
            result.setCode(exceptionCode);
            result.setDescription(errorMessage);
        } else {
            result.setCdr(previousResult.getCdr());
            result.setCode(previousResult.getCode());
            result.setStatus(previousResult.getStatus());
            result.setTicket(previousResult.getTicket());
            result.setDescription(previousResult.getDescription());
        }

        return result;
    }
}
