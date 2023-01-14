package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;

@Getter
public class CancelChallengeResponse {
    private String status;

    public CancelChallengeResponse(String status) {
        this.status = status;
    }
}
