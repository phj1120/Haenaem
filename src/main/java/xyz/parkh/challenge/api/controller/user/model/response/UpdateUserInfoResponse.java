package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;
import xyz.parkh.challenge.domain.user.entity.User;

@Getter
public class UpdateUserInfoResponse {
    private String name;

    public UpdateUserInfoResponse(User user) {
        name = user.getName();
    }
}
