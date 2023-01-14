package xyz.parkh.challenge.api.controller.user.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.WithdrawalDto;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class WithdrawalRequest {
    @NotNull
    private String authId;
    @NotNull
    private String password;

    public WithdrawalDto convertWithdrawal() {
        return new WithdrawalDto(authId, password);
    }
}
