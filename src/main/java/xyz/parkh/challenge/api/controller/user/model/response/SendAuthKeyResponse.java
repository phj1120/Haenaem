package xyz.parkh.challenge.api.controller.user.model.response;


import lombok.Getter;

@Getter
public class SendAuthKeyResponse {
    boolean state;

    public SendAuthKeyResponse(boolean state) {
        this.state = state;
    }
}
