package io.github.project.openubl.quickstart.xsender;

import io.github.project.openubl.xsender.XSender;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.models.SunatResponse;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        CompanyURLs companyURLs = CompanyURLs.builder()
                .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
                .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
                .despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")
                .build();

        CompanyCredentials credentials = CompanyCredentials.builder()
                .username("12345678959MODDATOS")
                .password("MODDATOS")
                .build();

        XSender xsender = new XSender(companyURLs, credentials);

        Path xml = Path.of(Main.class.getClassLoader().getResource("invoice.xml").toURI());
        SunatResponse response = xsender.send(xml);

        System.out.println("SUNAT status: " + response.getStatus());
    }
}
