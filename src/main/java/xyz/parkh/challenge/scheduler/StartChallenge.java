package xyz.parkh.challenge.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartChallenge {

    private final ChallengeScheduler challengeScheduler;

    @Scheduled(cron = "0 0 0 * * *")
    public void cronEveryMidnight() {
        LocalDate nowLocalDate = LocalDate.now();
        LocalDateTime start = LocalDateTime.now();
        log.info("[cronEveryMidnight - start] : {}", LocalDateTime.now());

        log.info("[challengeScheduler.start start] : {}", LocalDateTime.now());
        challengeScheduler.start(nowLocalDate);
        log.info("[challengeScheduler.start end] : {}", LocalDateTime.now());

        log.info("[challengeScheduler.end start] : {}", LocalDateTime.now());
        challengeScheduler.end(nowLocalDate);
        log.info("[challengeScheduler.end end] : {}", LocalDateTime.now());

        log.info("[cronEveryMidnight - End] : {}", LocalDateTime.now());
        LocalDateTime end = LocalDateTime.now();

        log.info("[cronEveryMidnight] : {} ms", Duration.between(start, end).getSeconds());
    }


}
