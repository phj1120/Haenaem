package xyz.parkh.challenge.api.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import xyz.parkh.challenge.InitDb;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.task.entity.Task;
import xyz.parkh.challenge.domain.task.repository.TaskRepository;
import xyz.parkh.challenge.domain.task.service.TaskService;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/test/")
@RequiredArgsConstructor
public class TestDataController {
    private final InitDb initDb;

    @Autowired
    ChallengeService challengeService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @Async
    @GetMapping("/add")
    public Result addData(@RequestParam(defaultValue = "10") Integer size) {
        for (int i = 0; i < size; i++) {
            initDb.init(size);
        }
        return Result.builder().data("ok").build();
    }

    @GetMapping("/add/task")
    public Result addTask(@RequestParam Long challengeHistoryId) {
        ChallengeHistory challengeHistory = challengeService.getChallengeHistory(challengeHistoryId);
        Task task1 = new Task("3차시", challengeHistory, LocalDate.now().minusDays(7), LocalDate.now());
        Task task2 = new Task("2차시", challengeHistory, LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));
        Task task3 = new Task("1차시", challengeHistory, LocalDate.now().minusDays(21), LocalDate.now().minusDays(14));
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        return Result.builder().data("ok").build();
    }


    @GetMapping
    public Sample test(@ModelAttribute Sample sample) {
        return sample;
    }

    @Getter
    static class Sample {

        private Long id;

        private String name;

        public Sample(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}
