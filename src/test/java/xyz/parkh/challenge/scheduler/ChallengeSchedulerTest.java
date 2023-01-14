package xyz.parkh.challenge.scheduler;

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
import xyz.parkh.challenge.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
@Transactional
class ChallengeSchedulerTest {
    private final ChallengeScheduler challengeScheduler;
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
    void startUsedBigQuery() {
        List<ChallengeHistory> start = challengeScheduler.start(LocalDate.now());

        Assertions.assertThat(start.size()).isEqualTo(3);
    }

    @Test
    void startUsedEachQuery() {
        List<ChallengeHistory> start = challengeScheduler.startUsedEachQuery(LocalDate.now());

        Assertions.assertThat(start.size()).isEqualTo(3);
    }

    @Test
    void end() {
        List<ChallengeHistory> start = challengeScheduler.start(LocalDate.now());
        Assertions.assertThat(start.size()).isEqualTo(3);

        List<ChallengeHistory> end = challengeScheduler.end(LocalDate.now());
        Assertions.assertThat(end.size()).isEqualTo(2);
    }

}