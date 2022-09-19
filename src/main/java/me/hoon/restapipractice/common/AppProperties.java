package me.hoon.restapipractice.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@ConfigurationProperties("my-app")
@Component
public class AppProperties {

    @NotBlank
    private String adminUsername;

    @NotBlank
    private String adminPassword;

    @NotBlank
    private String userUsername;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;
}
