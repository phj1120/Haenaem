package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;

@Getter
public class ChangePasswordResponse {
    Long userId;

    public ChangePasswordResponse(Long userId) {
        this.userId = userId;
    }
}
