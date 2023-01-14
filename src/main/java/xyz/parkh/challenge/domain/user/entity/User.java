package xyz.parkh.challenge.domain.user.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.challenge.model.HistoryType;
import xyz.parkh.challenge.domain.point.entity.PointHistory;
import xyz.parkh.challenge.domain.point.model.PointHistoryType;
import xyz.parkh.challenge.domain.user.model.UpdateUserDto;
import xyz.parkh.challenge.domain.user.model.UserType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String authId;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<Authority> authorities = new HashSet<>();

    //    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointHistory> pointHistories = new ArrayList<>();

    //    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ChallengeHistory> challengeHistories = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId())
                && Objects.equals(authId, user.getAuthId())
                && Objects.equals(password, user.getPassword())
                && Objects.equals(name, user.getName())
                && Objects.equals(email, user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authId, password, name, email);
    }

    // ---- 연관 관계 편의 메서드 ---- //
    public PointHistory chargePoint(long amount) {
        PointHistory pointHistory = new PointHistory(amount, LocalDateTime.now(),
                PointHistoryType.CHARGE, this, null);
        pointHistories.add(pointHistory);

        return pointHistory;
    }

    public PointHistory usePoint(long amount, ChallengeHistory challengeHistory) {
        PointHistory pointHistory = new PointHistory(-1 * amount, LocalDateTime.now(),
                PointHistoryType.REQUEST, this, challengeHistory);
        pointHistories.add(pointHistory);

        return pointHistory;
    }


    public PointHistory refundPoint(long amount, ChallengeHistory challengeHistory) {
        PointHistory pointHistory = new PointHistory(amount, LocalDateTime.now(),
                PointHistoryType.REFUND, this, challengeHistory);
        pointHistories.add(pointHistory);

        return pointHistory;
    }

    public ChallengeHistory requestChallenge(Challenge challenge) {
        ChallengeHistory challengeHistory = new ChallengeHistory(challenge, this, HistoryType.REQUEST);
        challengeHistories.add(challengeHistory);

        return challengeHistory;
    }

    public static User make(String authId, String password, String name, String email, UserType... userTypes) {
        User user = new User(authId, password, name, email);

        for (UserType userType : userTypes) {
            Authority authority = new Authority(user, userType);
            user.authorities.add(authority);
        }

        return user;
    }

    private User(String authId, String password, String name, String email, Set<Authority> authorities) {
        this.authId = authId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.authorities = authorities;
    }

    private User(String authId, String password, String name, String email) {
        this.authId = authId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void update(UpdateUserDto updateUserDto) {
        if (updateUserDto.getName() != null) {
            this.name = updateUserDto.getName();
        }
        if (updateUserDto.getPassword() != null) {
            this.password = updateUserDto.getPassword();
        }
    }
}
