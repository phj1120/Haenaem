package xyz.parkh.challenge.domain.task;

import lombok.Getter;
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
import xyz.parkh.challenge.domain.task.entity.Task;
import xyz.parkh.challenge.domain.task.model.CheckStatus;
import xyz.parkh.challenge.domain.task.repository.TaskRepository;
import xyz.parkh.challenge.domain.task.service.TaskService;
import xyz.parkh.challenge.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class TaskTests {
    private final InitTestDB initTestDB;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

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
    public void getChallengeHistory() {
        Task task1 = new Task("3차시", challengeHistory1, LocalDate.now().minusDays(7), LocalDate.now());
        Task task2 = new Task("2차시", challengeHistory1, LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));
        Task task3 = new Task("1차시", challengeHistory1, LocalDate.now().minusDays(21), LocalDate.now().minusDays(14));
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        task2.setCheckStatus(CheckStatus.REQUEST);

        List<Task> list = taskRepository.findByChallengeHistoryId(challengeHistory1.getId());
        Assertions.assertThat(list.size()).isEqualTo(3);

        task1.setCheckStatus(CheckStatus.SUCCESS);

        Challenge challenge = task1.getChallengeHistory().getChallenge();

        List<Task> tasks = taskRepository.findByChallengeId(challenge.getId());
        List<Task> checkedTask = findCheckedTask(tasks);
        List<Task> unCheckedTask = findUnCheckedTask(tasks);

        System.out.println(checkedTask);
        System.out.println(unCheckedTask);

        String name = task1.getChallengeHistory().getChallenge().getChallengeSet().getName();
    }

    @Test
    public void test() {
        List<Task> tasks = taskRepository.findByChallengeId(3L);
        List<Task> checkedTask = findCheckedTask(tasks);
        List<Task> unCheckedTask = findUnCheckedTask(tasks);

        System.out.println(checkedTask);
        System.out.println(unCheckedTask);
    }

    private List<Task> findUnCheckedTask(List<Task> tasks) {
        List<Task> checkedTask = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getCheckStatus() == CheckStatus.REQUEST) {
                checkedTask.add(task);
            }
        }
        return checkedTask;
    }

    private static List<Task> findCheckedTask(List<Task> tasks) {
        List<Task> checkedTask = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getCheckStatus() == CheckStatus.SUCCESS) {
                checkedTask.add(task);
            }
        }
        return checkedTask;
    }

    @Test
    public void taskToTaskInfo() {
        List<Task> tasks = taskRepository.findByChallengeHistoryId(challengeHistory1.getId());

        List<TaskInfo> taskInfos = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            taskInfos.add(new TaskInfo(tasks.get(i)));
        }
    }

    @Getter
    static class TaskInfo {
        private Long id;
        private String name;
        private LocalDate startSubmissionLocalDate;
        private LocalDate endSubmissionLocalDate;
        private CheckStatus checkStatus;
        private String imageName;

        public TaskInfo(Task task) {
            this.id = task.getId();
            this.name = task.getName();
            this.startSubmissionLocalDate = task.getStartSubmissionLocalDate();
            this.endSubmissionLocalDate = task.getEndSubmissionLocalDate();
            this.checkStatus = task.getCheckStatus();
            this.imageName = task.getImageName();
        }
    }

    @Test
    public void initTask() {
        List<Task> dailyChallengeTasks = taskService.initTask(challengeHistory1);
        Assertions.assertThat(dailyChallengeTasks.size()).isEqualTo(1);

        List<Task> weeklyChallengeTasks = taskService.initTask(challengeHistory3);
        int subjectNumberOfWeek = challengeHistory3.getChallenge().getChallengeSet().getSubjectNumberOfWeek();
        int weeks = challengeHistory3.getChallenge().getChallengeSet().getPeriodType().getDays() / 7;
        Assertions.assertThat(weeklyChallengeTasks.size()).isEqualTo(weeks * subjectNumberOfWeek);
    }
}
