package xyz.parkh.challenge.api.controller.challenge.model.response;


import lombok.Getter;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;

@Getter
public class CreateChallengeSetResponse {
    Long id;

    public CreateChallengeSetResponse(ChallengeSet challengeSet) {
        id = challengeSet.getId();
    }
}