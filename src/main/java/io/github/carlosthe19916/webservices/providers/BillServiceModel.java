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

package io.github.carlosthe19916.webservices.providers;

public class BillServiceModel {

    private Status status;
    private Integer code;
    private String description;
    private byte[] cdr;
    private String ticket;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public enum Status {
        ACEPTADO,
        RECHAZADO,
        BAJA,
        EXCEPCION,
        EN_PROCESO;

        public static Status fromCode(int code) {
            if (code == 0) {
                return Status.ACEPTADO;
            } else if (code >= 100 && code < 2_000) {
                return Status.EXCEPCION;
            } else if (code >= 2000 && code < 4000) {
                return Status.RECHAZADO;
            } else if (code >= 4000) {
                return Status.ACEPTADO;
            } else {
                return null;
            }
        }
    }

}
