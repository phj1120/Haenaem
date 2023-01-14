package xyz.parkh.challenge.domain.user.model;

import lombok.Data;

@Data
public class AuthKeyDto {
    private String authId;
    private String email;
    private MailAuthType type;

    public AuthKeyDto(String authId, String email, MailAuthType type) {
        this.authId = authId;
        this.email = email;
        this.type = type;
    }
}
