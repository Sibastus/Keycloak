package com.chernyshov.keycloak.connection;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Qualifier("broker")
public class KeycloakConnectionImpl implements KeycloakConnection{

    @Value("${keycloak.idmBrokerAuthUrl}")
    private String idmBrokerAuthUrl;

    @Value("${keycloak.idmBrokerApp}")
    private String idmBrokerApp;

    @Value("${keycloak.server.adminUser.username}")
    private String username;

    @Value("${keycloak.server.adminUser.password}")
    private String password;


    private static volatile Keycloak INSTANCE;

    @Override
    public Keycloak getKeycloakClient() {
        synchronized (KeycloakConnectionImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = KeycloakBuilder.builder()
                        .serverUrl(idmBrokerAuthUrl)
                        .realm("master")
                        .grantType(OAuth2Constants.PASSWORD)
                        .clientId(idmBrokerApp)
                        .username(username)
                        .password(password)
                        .build();
            }
        }

        return INSTANCE;
    }

    @PreDestroy
    public void close() {
        if (INSTANCE != null) {
            INSTANCE.close();
        }
    }
}
