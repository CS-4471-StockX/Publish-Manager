package com.stockx.publishmanagerws.configurations;

import com.stockx.publishmanagerws.mqtt.ClientConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ClientConnection clientConnection () {
        return new ClientConnection();
    }
}
