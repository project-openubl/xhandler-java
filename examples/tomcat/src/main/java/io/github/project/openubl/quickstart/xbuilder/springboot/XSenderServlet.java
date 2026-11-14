package io.github.project.openubl.quickstart.xbuilder.springboot;

import io.github.project.openubl.xsender.FileSender;
import io.github.project.openubl.xsender.Credentials;
import io.github.project.openubl.xsender.URLs;
import io.github.project.openubl.xsender.models.SunatResponse;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/api/file/upload")
public class XSenderServlet extends HttpServlet {

    private final FileSender xsender = new FileSender(
            URLs.builder()
                    .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
                    .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
                    .despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")
                    .build(),
            Credentials.builder()
                    .username("12345678959MODDATOS")
                    .password("MODDATOS")
                    .build()
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        byte[] xml;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("invoice.xml")) {
            xml = is.readAllBytes();
        }

        SunatResponse response = xsender.sendFile(xml);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.getWriter().write(response.getStatus().toString());
        resp.getWriter().flush();
    }
}
