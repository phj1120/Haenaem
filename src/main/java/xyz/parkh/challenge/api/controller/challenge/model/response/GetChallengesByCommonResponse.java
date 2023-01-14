package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;
import xyz.parkh.challenge.api.controller.challenge.model.ChallengeShortInfoForCommon;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetChallengesByCommonResponse {
    List<ChallengeShortInfoForCommon> challenges = new ArrayList();

    public GetChallengesByCommonResponse(List<ChallengeShortInfoForCommon> challenges) {
        this.challenges = challenges;
    }
}