package xyz.parkh.challenge.api.controller.user.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.SignUpUserDto;
import xyz.parkh.challenge.domain.user.model.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    @NotNull
    private String authId;

    @NotBlank
    private String password;

    @NotNull
    private String name;

    @Email
    private String email;

    private UserType userType = UserType.ROLE_CHALLENGER;

    public SignUpUserDto convertDto() {
        return new SignUpUserDto(authId, password, name, email, userType);
    }
}
