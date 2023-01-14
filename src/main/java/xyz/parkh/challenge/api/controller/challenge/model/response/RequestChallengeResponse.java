package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;

@Getter
public class RequestChallengeResponse {
    private long challengeHistoryId;

    public RequestChallengeResponse(ChallengeHistory challengeHistory) {
        this.challengeHistoryId = challengeHistory.getId();
    }
}
