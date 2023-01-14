package xyz.parkh.challenge.api.controller.challenge.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class RequestChallengeRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long challengeId;
}
