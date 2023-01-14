package xyz.parkh.challenge.domain.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpUserDto {
    private String authId;
    private String password;
    private String name;
    private UserType userType;
    private String email;

    public SignUpUserDto(String authId, String password, String name, String email, UserType userType) {
        this.authId = authId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.userType = userType;
    }
}
