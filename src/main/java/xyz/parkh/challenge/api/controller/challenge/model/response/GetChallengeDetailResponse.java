package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;

import java.time.LocalDate;

@Getter
public class GetChallengeDetailResponse {
    private long id;
    private int participantCount;
    private String name;
    private String info;
    private String image;
    private String description;
    private String content;
    private int subjectNumberOfWeek; // 주당 과제 제출 회수
    private ChallengePeriodType periodType;
    private LocalDate recruitmentStartLocalDate; // 모집 시작 일자
    private LocalDate recruitmentEndLocalDate; // 모집 종료 일자
    private LocalDate challengeStartLocalDate; // 챌린지 시작 일자
    private LocalDate challengeEndLocalDate; // 챌린지 종료 일자

    public GetChallengeDetailResponse(Challenge challenge) {
        this.id = challenge.getId();
        this.name = challenge.getChallengeSet().getName();
        this.info = challenge.getChallengeSet().getInfo();
        this.image = challenge.getChallengeSet().getImageUrl();
        this.recruitmentStartLocalDate = challenge.getRecruitmentStartLocalDate();
        this.recruitmentEndLocalDate = challenge.getRecruitmentEndLocalDate();
        this.challengeStartLocalDate = challenge.getChallengeStartLocalDate();
        this.challengeEndLocalDate = challenge.getChallengeEndLocalDate();
        this.periodType = challenge.getChallengeSet().getPeriodType();
        this.description = challenge.getChallengeSet().getDescription();
        this.subjectNumberOfWeek = challenge.getChallengeSet().getSubjectNumberOfWeek();
        this.content = challenge.getChallengeSet().getContent();
        this.participantCount = 5; // TODO 현재 참여 중인 인원
    }
}