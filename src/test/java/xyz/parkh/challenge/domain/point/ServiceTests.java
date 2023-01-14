package xyz.parkh.challenge.domain.point;

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
import xyz.parkh.challenge.domain.point.service.PointService;
import xyz.parkh.challenge.domain.user.entity.User;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class ServiceTests {
    private final PointService pointService;
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
    public void 포인트_충전() {
        Long beforeCharge = pointService.getPointAmount(user1);
        pointService.chargePoint(user1, 10000L);
        Long afterCharge = pointService.getPointAmount(user1);

        Assertions.assertThat(afterCharge - beforeCharge).isEqualTo(10000L);
    }

    @Test
    public void 포인트_사용() {
        Long beforeUsePoint = pointService.getPointAmount(user1);
        pointService.usePoint(user1, 10000L, challengeHistory1);
        Long afterUsePoint = pointService.getPointAmount(user1);

        Assertions.assertThat(beforeUsePoint - afterUsePoint).isEqualTo(10000L);
    }
}


