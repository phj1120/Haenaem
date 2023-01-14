package xyz.parkh.challenge.api.controller.user.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordRequest {
    @NotNull
    Long userId;
    @NotNull
    String authCode;
    @NotNull
    String newPassword;
}
