package com.victorgolovanov.TinkoffService.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "api")
public class Apiconfig {
    private Boolean isSandBoxMode;
}
