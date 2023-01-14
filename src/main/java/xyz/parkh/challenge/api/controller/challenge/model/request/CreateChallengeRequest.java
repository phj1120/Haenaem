package xyz.parkh.challenge.api.controller.challenge.model.request;

import lombok.Getter;
import lombok.Setter;
import xyz.parkh.challenge.domain.challenge.model.CreateChallengeDto;

import java.time.LocalDate;

@Getter
@Setter
public class CreateChallengeRequest {
    private Long challengeSetId;
    private LocalDate challengeStartLocalDate;
    private String stage;

    public CreateChallengeDto convert() {
        return new CreateChallengeDto(challengeSetId, challengeStartLocalDate, stage);
    }
}