package xyz.parkh.challenge.domain.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.UserType;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    public Authority(User user, UserType userType) {
        this.user = user;
        this.userType = userType;
    }

    // 연관 관게 편의 메서드
//    public static Authority make(User user, UserType userType) {
//        Authority authority = new Authority(user, userType);
//        user.getAuthorities().add(authority);
//
//        return authority;
//    }
}
