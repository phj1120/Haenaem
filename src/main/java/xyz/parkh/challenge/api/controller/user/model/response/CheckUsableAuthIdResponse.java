package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;

@Getter
public class CheckUsableAuthIdResponse {
    boolean isUsable;

    public CheckUsableAuthIdResponse(boolean isUsable) {
        this.isUsable = isUsable;
    }
}
