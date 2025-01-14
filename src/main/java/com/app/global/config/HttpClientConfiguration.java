package com.app.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Configuration
public class HttpClientConfiguration {

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }
}
