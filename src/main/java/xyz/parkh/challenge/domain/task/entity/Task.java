package xyz.parkh.challenge.domain.task.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.task.model.CheckStatus;
import xyz.parkh.challenge.domain.user.entity.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JoinColumn(name = "challenge_history_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChallengeHistory challengeHistory;


    private String imageName;

    private LocalDate startSubmissionLocalDate;

    private LocalDate endSubmissionLocalDate;

    private LocalDateTime submissionLocalDateTime;

    private LocalDateTime confirmationLocalDateTime;

    @JoinColumn(name = "admin_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    @Enumerated(value = EnumType.STRING)
    private CheckStatus checkStatus;

    public Task(String name, ChallengeHistory challengeHistory, LocalDate startSubmissionLocalDate, LocalDate endSubmissionLocalDate) {
        this.name = name;
        this.challengeHistory = challengeHistory;
        this.startSubmissionLocalDate = startSubmissionLocalDate;
        this.endSubmissionLocalDate = endSubmissionLocalDate;
    }
}
