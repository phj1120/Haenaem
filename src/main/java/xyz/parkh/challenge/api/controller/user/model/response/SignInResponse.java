package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;
import xyz.parkh.challenge.domain.user.model.SignInResultDto;

@Getter
public class SignInResponse {
    private Long id;
    private String name;
    private String authId;
    private String token;

    public SignInResponse(SignInResultDto signInResultDto) {
        this.id = signInResultDto.getId();
        this.authId = signInResultDto.getAuthId();
        this.name = signInResultDto.getName();
        this.token = signInResultDto.getToken();
    }
}
