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

package io.github.projectopenubl.xmlsenderlib.webservices.providers;

import io.github.projectopenubl.xmlsenderlib.webservices.wrappers.ServiceConfig;

import java.util.Map;

public interface BillServiceProvider {

    BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config);

    BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config);

    BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config, Map<String, Object> params, BillServiceCallback callback, long delay);

    BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config);

    BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config, Map<String, Object> params, BillServiceCallback callback, long delay);

    BillServiceModel getStatus(String ticket, ServiceConfig config);

}
