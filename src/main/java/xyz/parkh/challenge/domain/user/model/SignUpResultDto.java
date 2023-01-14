package xyz.parkh.challenge.domain.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpResultDto {
    private Long id;
    private String authId;
    private String name;

    public SignUpResultDto(User user) {
        this.id = user.getId();
        this.authId = user.getAuthId();
        this.name = user.getName();
    }
}
