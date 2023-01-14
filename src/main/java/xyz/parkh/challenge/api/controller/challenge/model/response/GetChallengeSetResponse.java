package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;

import java.util.List;

@Getter
public class GetChallengeSetResponse {
    private List<ChallengeSet> challengeSets;

    public GetChallengeSetResponse(List<ChallengeSet> challengeSets) {
        this.challengeSets = challengeSets;
    }
}

