package xyz.parkh.challenge.api.controller.user.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.MailAuthType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckAuthKeyRequest {
    @Email
    private String email;
    @NotNull
    private String authId;
    @NotNull
    private String authCode;
    @NotNull
    private MailAuthType type;

    public CheckAuthKeyRequest(String email, String authId, String authCode, MailAuthType type) {
        this.email = email;
        this.authId = authId;
        this.authCode = authCode;
        this.type = type;
    }
}
