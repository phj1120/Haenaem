package xyz.parkh.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.AddChallengeSetDto;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;
import xyz.parkh.challenge.domain.image.service.ImageService;
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
public class InitDb {

    private final InitService initService;

    //    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    public void init(int size) {
        initService.dbInit(size);
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final ChallengeService challengeService;
        private final AuthService authService;
        private final ImageService imageService;


        // 하려 하는 것 챌린지 셋 등록
        // Controller 에서 REQUEST 로 받아서,
        // dto 에 담아 Service 로 전달
        // Service 에서 Entity 로 만들어 Repository 에 전달

        public void dbInit() {
            // ChallengeSet 등록
            AddChallengeSetDto dailyAddChallengeSetDto = AddChallengeSetDto.createForDaily("Daily" + getUUID(5), "info", 10000L,
                    null, ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString());
            ChallengeSet dailyChallengeSet = challengeService.addChallengeSet(dailyAddChallengeSetDto);

            AddChallengeSetDto weeklyAddChallengeSetDto = AddChallengeSetDto.createForWeekly("weekly" + getUUID(5), "info", 10000L,
                    null, ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                    ChallengePeriodType.MONTHLY, 5);
            ChallengeSet weeklyChallengeSet = challengeService.addChallengeSet(weeklyAddChallengeSetDto);

            // Challenge 등록
            Challenge dailyChallenge = Challenge.makeChallenge(dailyChallengeSet, "1", LocalDate.now());
            challengeService.addChallenge(dailyChallenge);
            Challenge weeklyChallenge = Challenge.makeChallenge(weeklyChallengeSet, "1", LocalDate.now());
            challengeService.addChallenge(weeklyChallenge);

            // 유저 등록
            SignUpUserDto signUpUserDto = new SignUpUserDto("testUser" + getUUID(), "password", "name", "testUser" + getUUID() + "@AAA.com", UserType.ROLE_CHALLENGER);
            SignUpResultDto signUpResultDto = authService.signUp(signUpUserDto);
            User user = authService.findById(signUpResultDto.getId());

            // 챌린지 신청
            challengeService.requestChallenge(dailyChallenge, user);
            challengeService.requestChallenge(weeklyChallenge, user);
        }

        public void dbInit(int size) {
            for (int i = 0; i < size; i++) {
                dbInit();
            }
        }

        public String getUUID(int length) {
            return getUUID().substring(0, length);
        }

        public String getUUID() {
            return UUID.randomUUID().toString();
        }
    }
}
