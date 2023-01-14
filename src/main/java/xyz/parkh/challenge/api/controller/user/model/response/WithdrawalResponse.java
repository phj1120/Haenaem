package xyz.parkh.challenge.api.controller.user.model.response;

import lombok.Getter;

@Getter
public class WithdrawalResponse {
    boolean status;

    public WithdrawalResponse(boolean status) {
        this.status = status;
    }
}

