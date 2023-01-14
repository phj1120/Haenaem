package xyz.parkh.challenge.api.controller.challenge.model.request;

import lombok.Getter;
import lombok.Setter;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;

@Getter
@Setter
public class GetChallengeSetRequest {
    private ChallengeCategory category;

    public GetChallengeSetRequest(ChallengeCategory category) {
        this.category = category;
    }
}