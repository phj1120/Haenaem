package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;

@Getter
public class CheckUsableEmailResponse {
    boolean isUsable;

    public CheckUsableEmailResponse(boolean isUsable) {
        this.isUsable = isUsable;
    }
}
