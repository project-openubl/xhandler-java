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

import java.util.regex.Pattern;

public class Constants {

    public final static String DEFAULT_BILL_CONSULT_URL = "https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";
    public final static String DEFAULT_BILL_VALID_URL = "https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService";
    public final static Pattern FILENAME_STRUCTURE = Pattern.compile("(?:\\d{11}-)\\d{2}-[a-zA-Z_0-9]{4}-\\d{1,8}"); // [RUC]-[TIPO DOCUMENTO]-[SERIE]-[NUMERO]

}
