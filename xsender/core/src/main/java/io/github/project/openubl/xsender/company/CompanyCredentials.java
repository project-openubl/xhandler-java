package io.github.project.openubl.xsender.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CompanyCredentials {

    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;
    private final String token;
    
}
