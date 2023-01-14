package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;
import xyz.parkh.challenge.domain.user.entity.User;

@Getter
public class GetUserInfoResponse {
    private Long id;
    private String authId;
    private String name;
    private String email;

    public GetUserInfoResponse(User user) {
        this.id = user.getId();
        this.authId = user.getAuthId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
