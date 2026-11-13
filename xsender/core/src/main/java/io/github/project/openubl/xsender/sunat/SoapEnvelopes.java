package io.github.project.openubl.xsender.sunat;

import io.github.project.openubl.xsender.company.CompanyCredentials;

/**
 * The SOAP requests SUNAT's services accept, authenticated with a plain-text
 * WS-Security UsernameToken.
 */
final class SoapEnvelopes {

    private SoapEnvelopes() {
    }

    /** sendBill, sendSummary or sendPack */
    static String sendFile(String operation, CompanyCredentials credentials, String filename, String zipBase64) {
        return envelope(credentials, """
                <ns2:%1$s xmlns:ns2="http://service.sunat.gob.pe">
                    <fileName>%2$s</fileName>
                    <contentFile>%3$s</contentFile>
                </ns2:%1$s>""".formatted(operation, escape(filename), zipBase64));
    }

    static String getStatus(CompanyCredentials credentials, String ticket) {
        return envelope(credentials, """
                <ns2:getStatus xmlns:ns2="http://service.sunat.gob.pe">
                    <ticket>%s</ticket>
                </ns2:getStatus>""".formatted(escape(ticket)));
    }

    /** getStatus or getStatusCdr of the consult service */
    static String consultDocument(String operation, CompanyCredentials credentials, DocumentQuery query) {
        return envelope(credentials, """
                <ns2:%1$s xmlns:ns2="http://service.sunat.gob.pe">
                    <rucComprobante>%2$s</rucComprobante>
                    <tipoComprobante>%3$s</tipoComprobante>
                    <serieComprobante>%4$s</serieComprobante>
                    <numeroComprobante>%5$d</numeroComprobante>
                </ns2:%1$s>""".formatted(
                operation, escape(query.ruc()), escape(query.documentType()), escape(query.serie()), query.number()
        ));
    }

    static String validateCdp(CompanyCredentials credentials, CdpQuery query) {
        return envelope(credentials, """
                <ns2:validaCDPcriterios xmlns:ns2="http://service.sunat.gob.pe">
                    <rucEmisor>%s</rucEmisor>
                    <tipoCDP>%s</tipoCDP>
                    <serieCDP>%s</serieCDP>
                    <numeroCDP>%s</numeroCDP>
                    <tipoDocIdReceptor>%s</tipoDocIdReceptor>
                    <numeroDocIdReceptor>%s</numeroDocIdReceptor>
                    <fechaEmision>%s</fechaEmision>
                    <importeTotal>%s</importeTotal>
                    <nroAutorizacion>%s</nroAutorizacion>
                </ns2:validaCDPcriterios>""".formatted(
                escape(query.ruc()),
                escape(query.documentType()),
                escape(query.serie()),
                escape(query.number()),
                escape(query.receiverIdType()),
                escape(query.receiverIdNumber()),
                escape(query.issueDate()),
                query.total().toPlainString(),
                escape(query.authorizationNumber())
        ));
    }

    static String validateFile(CompanyCredentials credentials, String filename, String fileBase64) {
        return envelope(credentials, """
                <ns2:verificaCPEarchivo xmlns:ns2="http://service.sunat.gob.pe">
                    <nombre>%s</nombre>
                    <archivo>%s</archivo>
                </ns2:verificaCPEarchivo>""".formatted(escape(filename), fileBase64));
    }

    private static String envelope(CompanyCredentials credentials, String body) {
        return """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Header>
                <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
                <wsse:UsernameToken>
                <wsse:Username>%s</wsse:Username>
                <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">%s</wsse:Password>
                </wsse:UsernameToken>
                </wsse:Security>
                </soap:Header>
                <soap:Body>
                %s
                </soap:Body>
                </soap:Envelope>""".formatted(escape(credentials.getUsername()), escape(credentials.getPassword()), body);
    }

    static String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
