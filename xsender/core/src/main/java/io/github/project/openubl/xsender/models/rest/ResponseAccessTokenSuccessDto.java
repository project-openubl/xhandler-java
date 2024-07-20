package io.github.project.openubl.xsender.models.rest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAccessTokenSuccessDto {

    private String access_token;
    private int expires_in;
    private ZonedDateTime created_in;

}
