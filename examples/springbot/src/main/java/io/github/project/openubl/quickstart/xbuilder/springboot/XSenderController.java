package io.github.project.openubl.quickstart.xbuilder.springboot;

import io.github.project.openubl.xsender.XSender;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The XSender bean is configured with xsender.company.* in application.properties
 */
@RestController
public class XSenderController {

    private final XSender xsender;

    public XSenderController(XSender xsender) {
        this.xsender = xsender;
    }

    @PostMapping("/api/file/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        SunatResponse response = xsender.send(file.getBytes());
        return response.getStatus().toString();
    }

}
