package io.github.project.openubl.xsender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
public class Credentials {

    private final String username;
    @ToString.Exclude
    private final String password;
    private final String clientId;
    @ToString.Exclude
    private final String clientSecret;
    @ToString.Exclude
    private final String token;
}
