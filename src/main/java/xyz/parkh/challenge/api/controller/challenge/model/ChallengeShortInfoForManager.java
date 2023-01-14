package xyz.parkh.challenge.api.controller.challenge.model;

import lombok.Getter;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;

import java.time.LocalDate;

@Getter
public class ChallengeShortInfoForManager {
    private long id;
    private String name;
    private String image;
    private ChallengePeriodType periodType;
    private int subjectNumberOfWeek; // 주당 과제 제출 회수
    private LocalDate recruitmentStartLocalDate; // 모집 시작 일자
    private LocalDate recruitmentEndLocalDate; // 모집 종료 일자
    private LocalDate challengeStartLocalDate; // 챌린지 시작 일자
    private LocalDate challengeEndLocalDate; // 챌린지 종료 일자
    private int numberOfChallenger; // 참여중인 인원

    public ChallengeShortInfoForManager(Challenge challenge) {
        this.id = challenge.getId();
        this.name = challenge.getChallengeSet().getName();
        this.image = challenge.getChallengeSet().getImageUrl();
        this.recruitmentStartLocalDate = challenge.getRecruitmentStartLocalDate();
        this.recruitmentEndLocalDate = challenge.getRecruitmentEndLocalDate();
        this.challengeStartLocalDate = challenge.getChallengeStartLocalDate();
        this.challengeEndLocalDate = challenge.getChallengeEndLocalDate();
        this.periodType = challenge.getChallengeSet().getPeriodType();
        this.subjectNumberOfWeek = challenge.getChallengeSet().getSubjectNumberOfWeek();
        this.numberOfChallenger = 10; // TODO 현재 참여 중인 인원
    }
}
