package xyz.parkh.challenge.domain.challenge.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;
import xyz.parkh.challenge.domain.challenge.model.ChallengeProgressStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    private String stage; // 기수

    private LocalDate recruitmentStartLocalDate; // 모집 시작 일자

    private LocalDate recruitmentEndLocalDate; // 모집 종료 일자

    private LocalDate challengeStartLocalDate; // 챌린지 시작 일자

    private LocalDate challengeEndLocalDate; // 챌린지 종료 일자

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Challenge)) return false;
        Challenge challenge = (Challenge) o;
        return Objects.equals(id, challenge.getId())
                && Objects.equals(stage, challenge.getStage())
                && Objects.equals(recruitmentStartLocalDate, challenge.getRecruitmentStartLocalDate())
                && Objects.equals(recruitmentEndLocalDate, challenge.getRecruitmentEndLocalDate())
                && Objects.equals(challengeStartLocalDate, challenge.getChallengeStartLocalDate())
                && Objects.equals(challengeEndLocalDate, challenge.getChallengeEndLocalDate())
                && Objects.equals(challengeSet, challenge.getChallengeSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stage, recruitmentStartLocalDate, recruitmentEndLocalDate, challengeStartLocalDate, challengeEndLocalDate, challengeSet);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_set_id")
    private ChallengeSet challengeSet;

    private Challenge(ChallengeSet challengeSet, String stage,
                      LocalDate recruitmentStartLocalDate, LocalDate recruitmentEndLocalDate,
                      LocalDate challengeStartLocalDate, LocalDate challengeEndLocalDate) {
        this.challengeSet = challengeSet;
        this.stage = stage;
        this.recruitmentStartLocalDate = recruitmentStartLocalDate;
        this.recruitmentEndLocalDate = recruitmentEndLocalDate;
        this.challengeStartLocalDate = challengeStartLocalDate;
        this.challengeEndLocalDate = challengeEndLocalDate;
    }

    public static Challenge makeChallenge(ChallengeSet challengeSet, String stage,
                                          LocalDate challengeStartLocalDate) {
        ChallengePeriodType periodType = challengeSet.getPeriodType();
        LocalDate recruitmentStartLocalDate; // 모집 시작 일자
        LocalDate recruitmentEndLocalDate; // 모집 종료 일자
        LocalDate challengeEndLocalDate; // 챌린지 종료 일자
        if (periodType.equals(ChallengePeriodType.DAILY)) {
            // 일일 챌린지 : 시작일 1일 전 하루 동안 모집
            recruitmentStartLocalDate = challengeStartLocalDate.minusDays(1); // 모집 시작 일자
            recruitmentEndLocalDate = challengeStartLocalDate.minusDays(1); // 모집 종료 일자
            challengeEndLocalDate = challengeStartLocalDate; // 챌린지 종료 일자
        } else {
            // 일일 챌린지 이외의 챌린지 : 시작일 일주일 전부터 일주일 동안 모집
            recruitmentStartLocalDate = challengeStartLocalDate.minusDays(7); // 모집 시작 일자
            recruitmentEndLocalDate = challengeStartLocalDate.minusDays(1); // 모집 종료 일자
            challengeEndLocalDate = challengeStartLocalDate.plusDays(periodType.getDays() - 1); // 챌린지 종료 일자
        }

        return new Challenge(challengeSet, stage,
                recruitmentStartLocalDate, recruitmentEndLocalDate,
                challengeStartLocalDate, challengeEndLocalDate);
    }

    public ChallengeProgressStatus getStatus() {
        LocalDate now = LocalDate.now();
        if (now.isBefore(this.recruitmentStartLocalDate)) {
            return ChallengeProgressStatus.BEFORE_RECRUITMENT;
        } else if (now.isBefore(this.recruitmentEndLocalDate.plusDays(1))) {
            return ChallengeProgressStatus.RECRUITING;
        } else if (now.isBefore(this.challengeEndLocalDate.plusDays(1))) {
            return ChallengeProgressStatus.CHALLENGING;
        } else {
            return ChallengeProgressStatus.FINISHED;
        }
    }
}
