package com.zgr.notification.sender.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfig {

    @Value("${notification.send.connect-timeout-millis}")
    private int CONNECT_TIMEOUT;
    @Value("${notification.send.request-timeout-millis}")
    private int REQUEST_TIMEOUT;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        clientHttpRequestFactory.setConnectionRequestTimeout(REQUEST_TIMEOUT);
        return clientHttpRequestFactory;
    }
}
