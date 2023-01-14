package xyz.parkh.challenge.api.controller.user.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.AuthKeyDto;
import xyz.parkh.challenge.domain.user.model.MailAuthType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendAuthKeyRequest {
    private String authId;
    private String email;
    private MailAuthType type;

    public AuthKeyDto convertAuthKeyDto() {
        return new AuthKeyDto(this.authId, this.email, this.type);
    }
}
