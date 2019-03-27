package com.chernyshov.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserDto {

    private String name;
    private String login;
    private String password;
    private Boolean isBanned;
}
