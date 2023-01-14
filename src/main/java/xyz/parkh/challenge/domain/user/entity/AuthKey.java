package xyz.parkh.challenge.domain.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.AuthKeyDto;
import xyz.parkh.challenge.domain.user.model.MailAuthType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AuthKey {

    @Id
    @Column(name = "auth_key_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authId;

    private String email;

    private String authCode;

    @Enumerated(EnumType.STRING)
    private MailAuthType type;

    private LocalDateTime createTime = LocalDateTime.now();

    public AuthKey(AuthKeyDto authKeyDto) {
        this.authId = authKeyDto.getAuthId();
        this.email = authKeyDto.getEmail();
        this.type = authKeyDto.getType();
        this.authCode = UUID.randomUUID().toString().substring(0, 6);
    }
}
