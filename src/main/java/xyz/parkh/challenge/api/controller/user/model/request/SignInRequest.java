package xyz.parkh.challenge.api.controller.user.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.SignInDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {
    String authId;
    String password;

    public SignInDto convertSignInDto() {
        return new SignInDto(authId, password);
    }
}
