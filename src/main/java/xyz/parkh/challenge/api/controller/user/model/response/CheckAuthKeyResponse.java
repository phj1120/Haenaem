package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;

@Getter
public class CheckAuthKeyResponse {
    private boolean state;

    public CheckAuthKeyResponse(boolean state) {
        this.state = state;
    }
}
