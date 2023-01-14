package xyz.parkh.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.stereotype.Component;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.AddChallengeSetDto;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;
import xyz.parkh.challenge.domain.point.service.PointService;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.model.SignUpResultDto;
import xyz.parkh.challenge.domain.user.model.SignUpUserDto;
import xyz.parkh.challenge.domain.user.model.UserType;
import xyz.parkh.challenge.domain.user.service.AuthService;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitTestDB {
    private final ChallengeService challengeService;
    private final AuthService authService;
    private final PointService pointService;

    public ChallengeSet dailyChallengeSet;
    public ChallengeSet weeklyChallengeSet;
    public Challenge dailyChallenge;
    public Challenge weeklyChallenge;
    public User user1;
    public User user2;
    public ChallengeHistory challengeHistory1;
    public ChallengeHistory challengeHistory2;
    public ChallengeHistory challengeHistory3;


    @BeforeAll
    private void startLog() {
        log.debug("[InitDB] - Start");
    }

    @AfterAll
    private void endLog() {
        log.debug("[InitDB] - End");
    }

    public void setUp() {
        setUp(ChallengeCategory.LEARNING);
    }

    public void setUp(ChallengeCategory challengeCategory) {
        // ChallengeSet 등록
        AddChallengeSetDto dailyAddChallengeSetDto = AddChallengeSetDto.createForDaily("Daily" + getUUID(5), "info", 10000L,
                null, ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        dailyChallengeSet = challengeService.addChallengeSet(dailyAddChallengeSetDto);

        AddChallengeSetDto weeklyAddChallengeSetDto = AddChallengeSetDto.createForWeekly("weekly" + getUUID(5), "info", 10000L,
                null, ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                ChallengePeriodType.MONTHLY, 5);
        weeklyChallengeSet = challengeService.addChallengeSet(weeklyAddChallengeSetDto);

        // Challenge 등록
        dailyChallenge = Challenge.makeChallenge(dailyChallengeSet, "1-1", LocalDate.now());
        challengeService.addChallenge(dailyChallenge);

        weeklyChallenge = Challenge.makeChallenge(weeklyChallengeSet, "2-1", LocalDate.now());
        challengeService.addChallenge(weeklyChallenge);

        // 챌린저 등록
        String user1UUID = "authId" + UUID.randomUUID().toString().substring(0, 10);
        SignUpUserDto signUpUserDto1 = new SignUpUserDto("authId_" + user1UUID, "password",
                "name_" + user1UUID, "email_" + user1UUID + "@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto1 = authService.signUp(signUpUserDto1);
        user1 = authService.findById(signUpResultDto1.getId());


        String user2UUID = "authId" + UUID.randomUUID().toString().substring(0, 10);
        SignUpUserDto signUpUserDto2 = new SignUpUserDto("authId_" + user2UUID, "password",
                "name_" + user2UUID, "email_" + user2UUID + "@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto2 = authService.signUp(signUpUserDto2);
        user2 = authService.findById(signUpResultDto2.getId());

        pointService.chargePoint(user1, 20000L);
        pointService.chargePoint(user2, 20000L);

        // 챌린지 신청
        challengeHistory1 = challengeService.requestChallenge(dailyChallenge, user1);
        challengeHistory2 = challengeService.requestChallenge(dailyChallenge, user2);
        challengeHistory3 = challengeService.requestChallenge(weeklyChallenge, user2);
    }

    public String getUUID(int length) {
        return getUUID().substring(0, length);
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }
}
