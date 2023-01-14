package xyz.parkh.challenge.domain.challenge.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateChallengeDto {
    private Long challengeSetId;
    private LocalDate challengeStartLocalDate;
    private String stage;

    public CreateChallengeDto(Long challengeSetId, LocalDate challengeStartLocalDate, String stage) {
        this.challengeSetId = challengeSetId;
        this.challengeStartLocalDate = challengeStartLocalDate;
        this.stage = stage;
    }
}
