package xyz.parkh.challenge.api.controller.challenge.model.response;


import lombok.Getter;

@Getter
public class CreateChallengeResponse {
    private Long challengeId;

    public CreateChallengeResponse(Long challengeId) {
        this.challengeId = challengeId;
    }
}
