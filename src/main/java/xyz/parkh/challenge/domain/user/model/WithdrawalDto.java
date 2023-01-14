package xyz.parkh.challenge.domain.user.model;

import lombok.Getter;

@Getter
public class WithdrawalDto {
    String authId;
    String password;

    public WithdrawalDto(String authId, String password) {
        this.authId = authId;
        this.password = password;
    }
}
