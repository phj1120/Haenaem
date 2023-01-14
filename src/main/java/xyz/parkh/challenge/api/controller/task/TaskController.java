package xyz.parkh.challenge.api.controller.task;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.domain.image.service.ImageService;
import xyz.parkh.challenge.domain.task.entity.Task;
import xyz.parkh.challenge.domain.task.model.CheckStatus;
import xyz.parkh.challenge.domain.task.repository.TaskRepository;
import xyz.parkh.challenge.domain.task.service.TaskService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final ImageService imageService;


    //----------****사용자****----------
    //challenge history id 값으로 해당 과제 목록 나타내기
    @PreAuthorize("hasAnyRole('CHALLENGER')")
    @GetMapping("/{challengeHistoryId}")
    public GetTaskListResponse getTaskList(@PathVariable("challengeHistoryId") long id) {
        List<Task> tasks = taskRepository.findByChallengeHistoryId(id);
//        List<TaskInfo> collect = tasks.stream().map(task -> new TaskInfo(task.getId())).collect(Collectors.toList());
        String challengeName = "";
        List<TaskInfo> taskInfos = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            taskInfos.add(new TaskInfo(task));
            challengeName = task.getChallengeHistory().getChallenge().getChallengeSet().getName();
        }

        return new GetTaskListResponse(challengeName, taskInfos);
    }

    @Getter
    static class GetTaskListResponse {
        List<TaskInfo> taskInfos = new ArrayList<>();

        String challengeName;

        public GetTaskListResponse(String challengeName, List<TaskInfo> taskInfos) {
            this.taskInfos = taskInfos;
            this.challengeName = challengeName;
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

    //task id 값으로 해당 과제 상세 정보 나타내기
    @GetMapping("/details/{taskId}")
    @PreAuthorize("hasAnyRole('CHALLENGER')")
    public Result<GetTaskDetailInfoResponse> getTaskDetail(@PathVariable("taskId") long id) {
        Task task = taskService.getTask(id);
        String challengeName = "";
        challengeName = task.getChallengeHistory().getChallenge().getChallengeSet().getName();

        return Result.<GetTaskDetailInfoResponse>builder()
                .data(new GetTaskDetailInfoResponse(challengeName, task))
                .build();
    }

    @Data
    static class GetTaskDetailInfoResponse {

        private Long id;
        private String challengeName;
        private String name;

        private String imageName;
        private LocalDate startLocalDate;
        private LocalDate endLocalDate;

        private CheckStatus checkStatus;

        public GetTaskDetailInfoResponse(String challengeName, Task task) {
            this.id = task.getId();
            this.challengeName = challengeName;
            this.name = task.getName();
            this.imageName = task.getImageName();
            this.startLocalDate = task.getStartSubmissionLocalDate();
            this.endLocalDate = task.getEndSubmissionLocalDate();
            this.checkStatus = task.getCheckStatus();
        }
    }

    //과제 제출
    @PostMapping("/submission/{taskId}")
    @PreAuthorize("hasAnyRole('CHALLENGER')")
    public Result<SubmissionTaskResponse> submissionTask(@PathVariable("taskId") long id,
                                                         @ModelAttribute @Validated SubmissionTaskRequest request) throws IOException {
        String fileName = imageService.saveTask(request.file);

        taskService.submission(id, fileName);
        return Result.<SubmissionTaskResponse>builder()
                .data(new SubmissionTaskResponse(fileName)).build();
    }

    @Getter
    @Setter
    static class SubmissionTaskRequest {
        Long taskId;
        MultipartFile file;
    }

    @Getter
    static class SubmissionTaskResponse {
        String fileName;

        public SubmissionTaskResponse(String fileName) {
            this.fileName = fileName;
        }
    }

//
//----------****관리자****----------
//

    //과제제출 리스트
    @GetMapping("/taskSubmissionList/{challengeId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Result<GetTaskSubmissionListResponse> getTaskSubmissionList(@PathVariable("challengeId") long id) {
        List<Task> tasks = taskRepository.findByChallengeId(id);
//        List<TaskInfo> collect = tasks.stream().map(task -> new TaskInfo(task.getId())).collect(Collectors.toList());

        String challengeName = "";
        List<TaskSubmission> taskSubmissions = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            taskSubmissions.add(new TaskSubmission(task));
            challengeName = task.getChallengeHistory().getChallenge().getChallengeSet().getName();
        }

        return Result.<GetTaskSubmissionListResponse>builder()
                .data(new GetTaskSubmissionListResponse(challengeName, taskSubmissions)).build();
    }

//    @GetMapping("/taskSubmissionList/{challengeId}")
//    public GetTaskSubmissionListResponse getTaskSubmissionList(@PathVariable("challengeId") long id) {
//        List<Task> tasks = taskRepository.findByChallengeId(id);
////        List<TaskInfo> collect = tasks.stream().map(task -> new TaskInfo(task.getId())).collect(Collectors.toList());
//
//        List<TaskSubmission> taskSubmissions = new ArrayList<>();
//        for (int i = 0; i < tasks.size(); i++) {
//            Task task = tasks.get(i);
//            taskSubmissions.add(new TaskSubmission(task.getId()));
//        }
//
//        return new GetTaskSubmissionListResponse(taskSubmissions);
//    }

    @Getter
    static class GetTaskSubmissionListResponse {
        String challengeName;
        List<TaskSubmission> taskSubmission = new ArrayList<>();

        public GetTaskSubmissionListResponse(String challengeName, List<TaskSubmission> taskSubmission) {
            this.challengeName = challengeName;
            this.taskSubmission = taskSubmission;
        }

    }

    @Getter
    static class TaskSubmission {
        private Long id;
        private LocalDateTime submissionDateTime;
        //  private LocalDate startDate = LocalDate.now();
        private String fileName;


        public TaskSubmission(Task task) {
            this.id = task.getId();
            this.fileName = task.getImageName();
            this.submissionDateTime = task.getSubmissionLocalDateTime();
        }
    }

    //과제제출 상세
    @GetMapping("/taskSubmissionDetail/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Result<GetTaskSubmissionDetailResponse> getTaskSubmissionDetail(@PathVariable("taskId") long id) {
        Task tasks = taskService.getTask(id);
        String challengeName = "";
        challengeName = tasks.getChallengeHistory().getChallenge().getChallengeSet().getName();
        return Result.<GetTaskSubmissionDetailResponse>builder()
                .data(new GetTaskSubmissionDetailResponse(challengeName, tasks)).build();
    }

    @Getter
    static class GetTaskSubmissionDetailResponse {
        private Long id;
        private LocalDateTime submissionDateTime;
        private String ImageName;

        private String challengeName;

        public GetTaskSubmissionDetailResponse(String challengeName, Task task) {
            this.id = task.getId();
            this.challengeName = challengeName;
            this.submissionDateTime = task.getSubmissionLocalDateTime();
            this.ImageName = task.getImageName();
        }
    }


    //과제 승인, 반려
//    @GetMapping("/checkTask/{task_id}")
//    public String checkTask(@PathVariable("taskId") long id,
//                            @RequestParam("CheckStatus") String checkStatus) {
//        taskService.checkTask(id, checkStatus);
//        return checkStatus.toString();
//
//    }

    //    @GetMapping("/checkTask/{task_id}")
//    public Result<CheckTaskResponse> checkTask(@PathVariable("taskId") long id,
//                                       @ModelAttribute @Validated TaskController.CheckTaskRequest request) {
//       CheckStatus check = request.getCheck();
//        taskService.checkTask(id, check);
//        return  Result.<CheckTaskResponse>builder()
//                .data(new CheckTaskResponse(check)).build();
//
//    }
//
    @GetMapping("/checkTask/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Result<CheckTaskResponse> checkTask(@PathVariable("taskId") long id,
                                               @ModelAttribute CheckTaskRequest request) {
        CheckStatus check = request.getCheck();
        taskService.checkTask(id, check);
        return Result.<CheckTaskResponse>builder()
                .data(new CheckTaskResponse(check)).build();
    }

    @Getter
    @Setter
    static class CheckTaskRequest {

        CheckStatus check;
    }

    @Getter
    static class CheckTaskResponse {
        CheckStatus check;

        public CheckTaskResponse(CheckStatus check) {
            this.check = check;
        }
    }

    //과제 제출한 이미지
//    @PostMapping("taskSubmissionImage/{task_id}")
//    public String getTaskSubmissionImage(@PathVariable("taskId") long id) {
//        Task task = taskService.getTask(id);
//        String imageName = task.getImageName();
//        taskService.TaskImage(id);
////        String fileName = taskImage.getImageName();
////        taskService.TaskImage(id, fileName);
//    //   taskService.TaskImage(id);
//
//        return imageName;
//
    @GetMapping("taskSubmissionImage/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Result<TaskImageResponse> TaskImageResponse(@PathVariable("taskId") long id) {
        Task task = taskService.getTask(id);
        String imageName = task.getImageName();
//        String fileName = taskImage.getImageName();
//        taskService.TaskImage(id, fileName);
        //   taskService.TaskImage(id);
        return Result.<TaskImageResponse>builder().data(new TaskImageResponse(imageName)).build();
    }

    @Getter
    static class TaskImageResponse {
        private String imageName;

        public TaskImageResponse(String imageName) {
            this.imageName = imageName;
        }

    }
//    @Getter
//    static class TaskImageResuest {
//        private String imageName;
//
//        public TaskImageResuest(String imageName) {
//            this.imageName = imageName;
//        }


}