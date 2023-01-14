package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;
import xyz.parkh.challenge.api.controller.challenge.model.ChallengeHistoryShortInfoForChallenger;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetChallengesForChallengerResponse {
    List<ChallengeHistoryShortInfoForChallenger> challenges = new ArrayList<>();

    public GetChallengesForChallengerResponse(List<ChallengeHistoryShortInfoForChallenger> challenges) {
        this.challenges = challenges;
    }
}
