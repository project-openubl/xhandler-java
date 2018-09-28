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

package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.providers.BillServiceCallback;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Map;

public class MockBillServiceCallback implements BillServiceCallback {

    private final StatusWrapper statusWrapper;

    public MockBillServiceCallback() {
        this.statusWrapper = new StatusWrapper();
    }

    @Override
    public void onSuccess(Map<String, Object> params, int code, String description, byte[] cdr) {
        statusWrapper.setParams(params);

        statusWrapper.setStatus(BillServiceModel.Status.ACEPTADO);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
        statusWrapper.setCdr(cdr);
    }

    @Override
    public void onError(Map<String, Object> params, int code, String description, byte[] cdr) {
        statusWrapper.setParams(params);

        statusWrapper.setStatus(BillServiceModel.Status.RECHAZADO);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
        statusWrapper.setCdr(cdr);
    }

    @Override
    public void onProcess(Map<String, Object> params, int code, String description) {
        statusWrapper.setParams(params);

        statusWrapper.setStatus(BillServiceModel.Status.EN_PROCESO);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
    }

    @Override
    public void onException(Map<String, Object> params, int code, String description) {
        statusWrapper.setParams(params);

        statusWrapper.setStatus(BillServiceModel.Status.EXCEPCION);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
    }

    @Override
    public void onThrownException(Map<String, Object> params, SOAPFaultException exception) {

    }

    public StatusWrapper getStatusWrapper() {
        return statusWrapper;
    }

    public static class StatusWrapper {
        private BillServiceModel.Status status;
        private Integer code;
        private String description;
        private byte[] cdr;

        private Map<String, Object> params;

        public BillServiceModel.Status getStatus() {
            return status;
        }

        public void setStatus(BillServiceModel.Status status) {
            this.status = status;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public byte[] getCdr() {
            return cdr;
        }

        public void setCdr(byte[] cdr) {
            this.cdr = cdr;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }
    }
}
