package xyz.parkh.challenge.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.challenge.model.HistoryType;
import xyz.parkh.challenge.domain.challenge.repository.ChallengeHistoryRepository;
import xyz.parkh.challenge.domain.challenge.repository.ChallengeRepository;
import xyz.parkh.challenge.domain.task.service.TaskService;
import xyz.parkh.challenge.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class ChallengeScheduler {

    private final ChallengeService challengeService;
    private final TaskService taskService;
    private final ChallengeRepository challengeRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    // 챌린지 시작 기록 추가 및 과제 추가 : 각각 쿼리
    public List<ChallengeHistory> startUsedEachQuery(LocalDate localDate) {
        log.info("[Challenge Start] - start");
        List<ChallengeHistory> challengeHistories = new ArrayList<>();

        // 시작 하는 챌린지 리스트
        log.info("[Challenge Start] - get Challenges");
        List<Challenge> challenges = challengeRepository.findByChallengeStartLocalDate(localDate);

        for (Challenge challenge : challenges) {
            log.info("[Challenge Start] - {} get Challenger", challenge);
            // 해당 챌린지에 참여한 챌린저 조회
            Set<User> challengers = challengeService.findChallengers(challenge, HistoryType.REQUEST);

            // 참여한 챌린저 챌린지 시작 기록 추가
            for (User challenger : challengers) {
                log.info("[Challenge Start] - {} get request History", challenger);
                ChallengeHistory challengeHistory = challengeService.startChallenge(challenge, challenger);
                challengeHistories.add(challengeHistory);

                taskService.initTask(challengeHistory);
            }

        }

        return challengeHistories;
    }

    // 챌린지 시작 기록 추가 및 과제 추가 : 한방 쿼리
    public List<ChallengeHistory> start(LocalDate localDate) {
        log.info("[Challenge Start] - start");
        List<ChallengeHistory> challengeHistories = new ArrayList<>();

        // 시작 하는 챌린지 리스트
        log.info("[Challenge Start] - get Challenges");
        List<Challenge> challenges = challengeRepository.findByChallengeStartLocalDate(localDate);
        List<ChallengeHistory> requestChallengeHistories = challengeHistoryRepository.findAllByType(HistoryType.REQUEST);

        for (Challenge challenge : challenges) {
            log.info("[Challenge Start] - {} get Challenger", challenge);

            // 해당 챌린지에 참여한 챌린저 조회
            Set<User> challengers = challengeService.findChallengers(requestChallengeHistories, challenge);

            // 참여한 챌린저 챌린지 시작 기록 추가
            for (User challenger : challengers) {
                log.info("[Challenge Start] - {} get request History", challenger);
                ChallengeHistory challengeHistory = challengeService.startChallenge(challenge, challenger);
                challengeHistories.add(challengeHistory);

                taskService.initTask(challengeHistory);

            }
        }

        return challengeHistories;
    }


    // 챌린지 종료 기록 추가 및 결과 추가
    public List<ChallengeHistory> end(LocalDate localDate) {
        // 종료한 챌린지 리스트
        log.info("[Challenge End] - start");
        List<ChallengeHistory> challengeHistories = new ArrayList<>();

        // 종료된 챌린지 조회
        log.info("[Challenge End] - get Challenges");
        List<Challenge> endChallenges = challengeRepository.findByChallengeEndLocalDate(localDate);
        List<ChallengeHistory> challengingChallengeHistories = challengeHistoryRepository.findAllByType(HistoryType.START);

        // 참여한 챌린저 챌린지 종료 기록 추가
        for (Challenge challenge : endChallenges) {
            log.info("[Challenge End] - {} get Challenger", challenge);

            // 해당 챌린지에 참여한 챌린저 조회
            Set<User> challengers = challengeService.findChallengers(challengingChallengeHistories, challenge);

            // 챌린지 종료 기록 추가
            for (User challenger : challengers) {
                log.info("[Challenge End] - {} get request History", challenger);
                ChallengeHistory endChallengeHistory = challengeService.endChallenge(challenge, challenger);
                challengeHistories.add(endChallengeHistory);
            }
        }
        log.info("[Challenge End] - end");

        return challengeHistories;
    }
}
