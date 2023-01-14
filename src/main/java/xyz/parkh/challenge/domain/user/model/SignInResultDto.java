package xyz.parkh.challenge.domain.user.model;

import lombok.Data;
import xyz.parkh.challenge.domain.user.entity.User;

@Data
public class SignInResultDto {
    private Long id;
    private String authId;
    private String name;
    private String token;

    public SignInResultDto(User user, String token) {
        this.id = user.getId();
        this.authId = user.getAuthId();
        this.name = user.getName();
        this.token = token;
    }

}
