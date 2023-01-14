package xyz.parkh.challenge.api.controller.challenge.model.request;

import lombok.Getter;
import lombok.Setter;
import xyz.parkh.challenge.domain.challenge.model.ChallengeProgressStatus;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class GetChallengesForChallengerRequest {
    @NotNull
    private ChallengeProgressStatus status;
}