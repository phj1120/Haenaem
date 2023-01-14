package xyz.parkh.challenge.domain.point.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.point.model.PointHistoryType;
import xyz.parkh.challenge.domain.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long id;

    private Long amount;

    private LocalDateTime dateTime;

    @Enumerated(value = EnumType.STRING)
    private PointHistoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_history_id")
    private ChallengeHistory challengeHistory;

    @Builder
    public PointHistory(Long amount, LocalDateTime dateTime, PointHistoryType type, User user, ChallengeHistory challengeHistory) {
        this.amount = amount;
        this.dateTime = dateTime;
        this.type = type;
        this.user = user;
        this.challengeHistory = challengeHistory;
    }

//    public static PointHistory chargePoint(User user, long amount) {
//        return PointHistory.builder()
//                .user(user).amount(amount)
//                .type(PointHistoryType.CHARGE)
//                .dateTime(LocalDateTime.now()).build();
//    }
//
//    public static PointHistory usePoint(User user, long amount, ChallengeHistory challengeHistory) {
//        return PointHistory.builder()
//                .user(user).amount(-1 * amount)
//                .type(PointHistoryType.REQUEST)
//                .challengeHistory(challengeHistory)
//                .dateTime(LocalDateTime.now()).build();
//    }
//
//    public static PointHistory refundPoint(User user, long amount, ChallengeHistory challengeHistory) {
//        return PointHistory.builder()
//                .user(user).amount(amount)
//                .type(PointHistoryType.CANCEL)
//                .challengeHistory(challengeHistory)
//                .dateTime(LocalDateTime.now()).build();
//    }


}
