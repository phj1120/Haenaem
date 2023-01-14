package xyz.parkh.challenge.domain.user.model;

import lombok.Getter;

@Getter
public class SignInDto {
    String authId;
    String password;

    public SignInDto(String authId, String password) {
        this.authId = authId;
        this.password = password;
    }
}
