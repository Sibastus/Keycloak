package com.chernyshov.messaging;


import com.chernyshov.dto.ChatUserDto;
import com.chernyshov.messaging.payload.Payload;
import com.chernyshov.service.UserService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(UserInput.class)
@Setter
@NoArgsConstructor
public class UserListener {
    @Autowired
    private UserService userService;

    @StreamListener(target = UserInput.INPUT)
    public void onTenantEvent(Message<Payload> message) {

        Payload event = message.getPayload();
        ChatUserDto user = event.getChatUserDto();
        switch (event.getEvent()) {
            case "CREATE":
                userService.insert(user);
                break;
            default:
                break;
        }
    }
}
