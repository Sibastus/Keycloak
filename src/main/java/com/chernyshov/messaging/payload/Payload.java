package com.chernyshov.messaging.payload;


import com.chernyshov.dto.ChatUserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Payload {
    private ChatUserDto chatUserDto;
    private String event;
}
