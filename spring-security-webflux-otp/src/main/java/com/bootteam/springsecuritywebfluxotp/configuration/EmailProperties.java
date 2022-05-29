package com.bootteam.springsecuritywebfluxotp.configuration;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * email configuration properties
 */
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "app.mail")
public class EmailProperties {

    private String from;

    private String baseUrl;

}
