package com.example.discordia.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
public class UserDto {

    private UUID UserId;
    private String firstname;
    private String lastname;

    private String username;
    private String displayName;
    private String userBio;

    private String imgUrl;

    private String password;
    private String email;
}
