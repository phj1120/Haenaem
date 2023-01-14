package xyz.parkh.challenge.domain.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;
import xyz.parkh.challenge.domain.task.entity.Task;
import xyz.parkh.challenge.domain.task.model.CheckStatus;
import xyz.parkh.challenge.domain.task.repository.TaskRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> initTask(ChallengeHistory challengeHistory) {
        List<Task> tasks = new ArrayList<>();

        Challenge challenge = challengeHistory.getChallenge();
        LocalDate challengeStartLocalDate = challenge.getChallengeStartLocalDate();
        LocalDate challengeEndLocalDate = challenge.getChallengeEndLocalDate();

        ChallengeSet challengeSet = challengeHistory.getChallenge().getChallengeSet();
        ChallengePeriodType periodType = challengeSet.getPeriodType();
        int subjectNumberOfWeek = challengeSet.getSubjectNumberOfWeek();

        // 일일 챌린지 - 과제 한 개, 당일에만 제출 가능
        if (periodType == ChallengePeriodType.DAILY) {
            Task task = new Task("1차시", challengeHistory, challengeStartLocalDate, challengeEndLocalDate);
            tasks.add(task);
            taskRepository.save(task);
        }

        // 주간 챌린지 - 주차 * 주당 과제 제출 개수, 해당 주 까지 제출 가능
        int totalWeeks = periodType.getDays() / 7;
        // 주차별 과제 생성
        for (int i = 0; i < totalWeeks; i++) {
            // 해당 주차의 차시 과제 생성
            for (int j = 0; j < subjectNumberOfWeek; j++) {
                LocalDate startSubmissionTaskLocalDate = challengeStartLocalDate.plusDays(i * 7);
                LocalDate endSubmissionTaskLocalDate = startSubmissionTaskLocalDate.plusDays(7);
                Task task = new Task((i + 1) + "주차 " + (j + 1) + "차시", challengeHistory, startSubmissionTaskLocalDate, endSubmissionTaskLocalDate);
                challengeHistory.addTask(task);
                tasks.add(task);
            }
        }

        log.info("[initTask] : 챌린지 기록 ID : {} | 신청자 ID : {} | 과제 개수 {}", challengeHistory.getId(), challengeHistory.getUser().getId(), tasks.size());

        return tasks;
    }

    // 챌린지 정산
    public void calculatePoint() {
        // 100 : 예치금 + 실패한 사람들의 포인트
        // 80 ~ 99 : 예치금
        // 0 ~ 79 : 미지금
    }

    public List<Task> getChallengeHistoryId(long challengeHistoryId) {
        List<Task> task = taskRepository.findByChallengeHistoryId(challengeHistoryId);
        return task;
    }


    public Task getTask(long id) {
        return taskRepository.findById(id).orElseThrow();
    }


    public void submission(long id, String fileName) {
        Task task = getTask(id);
        task.setImageName(fileName);
        task.setCheckStatus(CheckStatus.REQUEST);
        task.setSubmissionLocalDateTime(LocalDateTime.now());
    }

    public void checkTask(long id, CheckStatus check) {
        Task task = getTask(id);
        task.setCheckStatus(check);
        task.setConfirmationLocalDateTime(LocalDateTime.now());
    }

//    public void TaskImage(long id){
//        Task task = getTask(id);
//        task.getImageName();
//    }
}
