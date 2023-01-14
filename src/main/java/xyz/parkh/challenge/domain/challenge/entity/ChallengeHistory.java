package xyz.parkh.challenge.domain.challenge.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.model.HistoryType;
import xyz.parkh.challenge.domain.task.entity.Task;
import xyz.parkh.challenge.domain.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_history_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private HistoryType type;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "challenge_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @OneToMany(mappedBy = "challengeHistory", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    private LocalDateTime dateTime = LocalDateTime.now();

    public ChallengeHistory(Challenge challenge, User user, HistoryType type) {
        this.type = type;
        this.user = user;
        this.challenge = challenge;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setChallengeHistory(this);
    }

    public ChallengeCategory getCategory() {
        return this.challenge.getChallengeSet().getCategory();
    }
}
