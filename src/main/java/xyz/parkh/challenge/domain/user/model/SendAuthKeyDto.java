package xyz.parkh.challenge.domain.user.model;

import lombok.Data;
import xyz.parkh.challenge.domain.user.entity.AuthKey;

@Data
public class SendAuthKeyDto {
    private String authId;
    private String email;
    private String authCode;

    public SendAuthKeyDto(AuthKey authKey) {
        this.authId = authKey.getAuthId();
        this.email = authKey.getEmail();
        this.authCode = authKey.getAuthCode();
    }
}
