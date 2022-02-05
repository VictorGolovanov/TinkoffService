package com.victorgolovanov.TinkoffService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApi;


@Configuration
@EnableConfigurationProperties(Apiconfig.class)
@RequiredArgsConstructor
public class ApplicationConfig {

    private final Apiconfig apiconfig;

    @Bean
    public OpenApi api() {
        String ssoToken = System.getenv("ssoToken");
        return new OkHttpOpenApi(ssoToken, apiconfig.getIsSandBoxMode());
    }
}
