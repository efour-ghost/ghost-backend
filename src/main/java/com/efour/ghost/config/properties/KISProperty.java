package com.efour.ghost.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kis")
public class KISProperty {
    private String url;
    private String port;
    private String appkey;
    private String appsecret;
    private String custType;
    private String contentType;
    private String trId;

}
