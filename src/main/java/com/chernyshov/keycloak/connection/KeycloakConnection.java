package com.chernyshov.keycloak.connection;

import org.keycloak.admin.client.Keycloak;

public interface KeycloakConnection {

    Keycloak getKeycloakClient();
}
