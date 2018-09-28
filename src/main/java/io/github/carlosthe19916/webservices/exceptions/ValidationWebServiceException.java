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

import io.github.carlosthe19916.webservices.utils.SunatErrors;
import io.github.carlosthe19916.webservices.utils.Utils;

import javax.xml.ws.soap.SOAPFaultException;

public class ValidationWebServiceException extends AbstractWebServiceException {

    public ValidationWebServiceException(SOAPFaultException exception) {
        super(exception);
    }

    public Integer getSUNATErrorCode() {
        return Utils.getErrorCode(exception).orElse(-1);
    }

    public String getSUNATErrorMessage() {
        Integer errorCode = getSUNATErrorCode();
        return SunatErrors.getInstance().get(errorCode);
    }

    public String getSUNATErrorMessage(int maxLength) {
        Integer errorCode = getSUNATErrorCode();
        return SunatErrors.getInstance().getWithMaxLength(errorCode, maxLength);
    }

}
