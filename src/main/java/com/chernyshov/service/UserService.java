package com.chernyshov.service;



import com.chernyshov.dto.ChatUserDto;
import com.chernyshov.keycloak.connection.KeycloakConnection;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    @Qualifier("broker")
    private KeycloakConnection keycloakConnection;

    @Value("${keycloak.appRealm}")
    private String realm;

    private final static String clientid = "chat-service";

    public void insert(ChatUserDto chatUserDto){
        //create user
        UserRepresentation userRepresentation =new UserRepresentation();
        userRepresentation.setLastName(chatUserDto.getName());
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(chatUserDto.getLogin());
        keycloakConnection.getKeycloakClient().realm(realm).users().create(userRepresentation);

        //add password for user
        List<UserRepresentation> users = keycloakConnection.getKeycloakClient().realm(realm).users().search(chatUserDto.getLogin());
        UserRepresentation user = users.get(0);

        CredentialRepresentation credentialRepresentation =new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(chatUserDto.getPassword());
        keycloakConnection.getKeycloakClient().realm(realm).users().get(user.getId()).resetPassword(credentialRepresentation);

        //add role ROLE_USER
        List<ClientRepresentation> clients = keycloakConnection.getKeycloakClient().realm(realm).clients().findByClientId(clientid);
        ClientRepresentation client = clients.get(0);
        List<RoleRepresentation> roles = keycloakConnection.getKeycloakClient().realm(realm).clients().get(client.getId()).roles().list();

        RoleRepresentation role = roles.stream().filter(r -> r.getName().equals("ROLE_USER")).findFirst().get();
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setContainerId(client.getId());
        roleRepresentation.setClientRole(true);
        roleRepresentation.setComposite(false);
        roleRepresentation.setName("ROLE_USER");
        roleRepresentation.setId(role.getId());
        keycloakConnection.getKeycloakClient().realm(realm).users().get(user.getId()).roles().clientLevel(client.getId()).add(Arrays.asList(roleRepresentation));
    }


}
