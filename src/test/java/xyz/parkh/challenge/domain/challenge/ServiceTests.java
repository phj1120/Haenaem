package xyz.parkh.challenge.domain.challenge;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.InitTestDB;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.*;
import xyz.parkh.challenge.domain.user.entity.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@RequiredArgsConstructor
@Transactional
public class ServiceTests {
    private final ChallengeService challengeService;
    private final InitTestDB initTestDB;

    public ChallengeSet dailyChallengeSet;
    public ChallengeSet weeklyChallengeSet;
    public Challenge dailyChallenge;
    public Challenge weeklyChallenge;
    public User user1;
    public User user2;
    public ChallengeHistory challengeHistory1;
    public ChallengeHistory challengeHistory2;
    public ChallengeHistory challengeHistory3;

    @BeforeEach
    public void init() {
        initTestDB.setUp();

        dailyChallengeSet = initTestDB.dailyChallengeSet;
        weeklyChallengeSet = initTestDB.weeklyChallengeSet;

        dailyChallenge = initTestDB.dailyChallenge;
        weeklyChallenge = initTestDB.weeklyChallenge;

        user1 = initTestDB.user1;
        user2 = initTestDB.user2;

        challengeHistory1 = initTestDB.challengeHistory1;
        challengeHistory2 = initTestDB.challengeHistory2;
        challengeHistory3 = initTestDB.challengeHistory3;
    }

    @Test
    public void 일일_챌린지_생성() {
        // ChallengeSet 등록
        AddChallengeSetDto addChallengeSetDto = AddChallengeSetDto.createForDaily("DailyChallenge", "info", 10000L, null,
                ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        ChallengeSet saveChallengeSet = challengeService.addChallengeSet(addChallengeSetDto);

        // Challenge 등록
        CreateChallengeDto createChallengeDto = new CreateChallengeDto(saveChallengeSet.getId(), LocalDate.now(), "1-1");
        Challenge saveChallenge = challengeService.addChallenge(createChallengeDto);

        Assertions.assertThat(ChronoUnit.DAYS.between(saveChallenge.getChallengeStartLocalDate(), saveChallenge.getChallengeEndLocalDate()))
                .isEqualTo(saveChallenge.getChallengeSet().getPeriodType().getDays() - 1);
    }

    @Test
    public void 주간_챌린지_생성() {
        // ChallengeSet 등록
        AddChallengeSetDto addChallengeSetDto = AddChallengeSetDto.createForWeekly("WeeklyChallenge", "info", 10000L, null,
                ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                ChallengePeriodType.WEEKLY, 5);
        ChallengeSet saveChallengeSet = challengeService.addChallengeSet(addChallengeSetDto);

        // Challenge 등록
        CreateChallengeDto createChallengeDto = new CreateChallengeDto(saveChallengeSet.getId(), LocalDate.now(), "1-1");
        Challenge saveChallenge = challengeService.addChallenge(createChallengeDto);

        Assertions.assertThat(ChronoUnit.DAYS.between(saveChallenge.getChallengeStartLocalDate(), saveChallenge.getChallengeEndLocalDate()))
                .isEqualTo(saveChallenge.getChallengeSet().getPeriodType().getDays() - 1);
    }

    @Test
    public void 월간_챌린지_생성() {
        // ChallengeSet 등록
        AddChallengeSetDto addChallengeSetDto = AddChallengeSetDto.createForWeekly("MonthlyChallenge", "info", 10000L, null,
                ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                ChallengePeriodType.MONTHLY, 5);
        ChallengeSet saveChallengeSet = challengeService.addChallengeSet(addChallengeSetDto);

        // Challenge 등록
        CreateChallengeDto createChallengeDto = new CreateChallengeDto(saveChallengeSet.getId(), LocalDate.now(), "1-1");
        Challenge saveChallenge = challengeService.addChallenge(createChallengeDto);

        Assertions.assertThat(ChronoUnit.DAYS.between(saveChallenge.getChallengeStartLocalDate(), saveChallenge.getChallengeEndLocalDate()))
                .isEqualTo(saveChallenge.getChallengeSet().getPeriodType().getDays() - 1);
    }

    @Test
    public void 백일_챌린지_생성() {
        // ChallengeSet 등록
        AddChallengeSetDto addChallengeSetDto = AddChallengeSetDto.createForWeekly("HundredChallenge", "info", 10000L, null,
                ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                ChallengePeriodType.HUNDRED, 5);
        ChallengeSet saveChallengeSet = challengeService.addChallengeSet(addChallengeSetDto);

        // Challenge 등록
        CreateChallengeDto createChallengeDto = new CreateChallengeDto(saveChallengeSet.getId(), LocalDate.now(), "1-1");
        Challenge saveChallenge = challengeService.addChallenge(createChallengeDto);

        Assertions.assertThat(ChronoUnit.DAYS.between(saveChallenge.getChallengeStartLocalDate(), saveChallenge.getChallengeEndLocalDate()))
                .isEqualTo(saveChallenge.getChallengeSet().getPeriodType().getDays() - 1);
    }


    @Test
    public void 챌린지_신청_및_취소() {
        // 신청 확인
        Set<User> requestChallengers = challengeService.findChallengers(dailyChallenge, HistoryType.REQUEST);
        assertEquals(2, requestChallengers.size());

        // Challenge 신청 취소
        challengeService.cancelChallenge(dailyChallenge, challengeHistory2.getUser());

        // 취소 확인
        Set<User> afterCancelChallengers = challengeService.findChallengers(dailyChallenge, HistoryType.REQUEST);
        assertEquals(1, afterCancelChallengers.size());
    }

    @Test
    public void 진행_중인_테스트_조회() {
        List<Challenge> challengeListChallenging = challengeService.getChallengeListChallenging(ChallengeCategory.LEARNING);
        for (Challenge challenge : challengeListChallenging) {
            ChallengeSet challengeSet = challenge.getChallengeSet();
            System.out.println(challengeSet);
            String name = challengeSet.getName();
            System.out.println(name);
        }
        assertNotEquals(0, challengeListChallenging.size());
    }

    @Test
    public void 사용자_챌린지_조회() {
        List<Challenge> user1ChallengeList = challengeService.getUserChallengeList(user1, ChallengeCategory.LEARNING);
        List<Challenge> user2ChallengeList = challengeService.getUserChallengeList(user2, ChallengeCategory.LEARNING);

        for (Challenge challenge : user2ChallengeList) {
            ChallengeSet challengeSet = challenge.getChallengeSet();
            System.out.println(challengeSet);
            String name = challengeSet.getName();
            System.out.println(name);
        }

        assertEquals(1, user1ChallengeList.size());
        assertEquals(2, user2ChallengeList.size());
    }

    @Test
    public void 카테고리_여러개_조회() {
        Set<ChallengeCategory> categorySet = new HashSet<ChallengeCategory>();
        categorySet.add(ChallengeCategory.LEARNING);
        categorySet.add(ChallengeCategory.EXERCISE);
        Map<ChallengeCategory, List<Challenge>> userChallengeMap = challengeService.getUserChallengeList(user2, categorySet);
        for (ChallengeCategory challengeCategory : userChallengeMap.keySet()) {
            System.out.println(challengeCategory);
            for (Challenge challenge : userChallengeMap.get(challengeCategory)) {
                System.out.println(challenge);
            }
        }
        assertEquals(2, userChallengeMap.size());
        assertEquals(2, userChallengeMap.get(ChallengeCategory.LEARNING).size());
        assertEquals(0, userChallengeMap.get(ChallengeCategory.EXERCISE).size());
        assertEquals(null, userChallengeMap.get(ChallengeCategory.ENVIRONMENT));
    }

    @Test
    public void 카테고리_여러개_가변_인자_이용해_조회_() {
        // 가변 인자 사용법 1
//        ChallengeCategory[] categories = {ChallengeCategory.LEARNING, ChallengeCategory.EXERCISE};
//        Map<ChallengeCategory, List<Challenge>> userChallengeMap = challengeService.getUserChallengeList(user, categories);

        // 가변 인자 사용법 2
        Map<ChallengeCategory, List<Challenge>> userChallengeMap = challengeService.getUserChallengeList(user2, ChallengeCategory.LEARNING, ChallengeCategory.EXERCISE);

        for (ChallengeCategory challengeCategory : userChallengeMap.keySet()) {
            System.out.println(challengeCategory);
            for (Challenge challenge : userChallengeMap.get(challengeCategory)) {
                System.out.println(challenge);
            }
        }
        assertEquals(2, userChallengeMap.size());
        assertEquals(2, userChallengeMap.get(ChallengeCategory.LEARNING).size());
        assertEquals(0, userChallengeMap.get(ChallengeCategory.EXERCISE).size());
        assertEquals(null, userChallengeMap.get(ChallengeCategory.ENVIRONMENT));
    }


    @Test
    public void 챌린지_신청한_사용자_조회() {
        Set<User> dailyUsers = challengeService.findChallengers(dailyChallenge, HistoryType.REQUEST);
        Set<User> weeklyUsers = challengeService.findChallengers(weeklyChallenge, HistoryType.REQUEST);

        org.assertj.core.api.Assertions.assertThat(dailyUsers).hasSize(2);
        org.assertj.core.api.Assertions.assertThat(weeklyUsers).hasSize(1);
    }
}
