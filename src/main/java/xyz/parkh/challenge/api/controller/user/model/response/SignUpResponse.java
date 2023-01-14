package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;
import xyz.parkh.challenge.domain.user.entity.User;

@Getter
public class SignUpResponse {
    private Long id;
    private String name;

    public SignUpResponse(User user) {
        id = user.getId();
        name = user.getName();
    }
}
