package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;
import xyz.parkh.challenge.api.controller.challenge.model.ChallengeShortInfoForManager;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetChallengesForManagerResponse {
    List<ChallengeShortInfoForManager> challenges = new ArrayList();

    public GetChallengesForManagerResponse(List<ChallengeShortInfoForManager> challenges) {
        this.challenges = challenges;
    }
}
